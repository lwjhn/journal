package com.rongji.egov.journal.service.controller;

import com.rongji.egov.journal.service.excel.input.ImportConfig;
import com.rongji.egov.journal.service.excel.input.ImportExecutor;
import com.rongji.egov.journal.service.excel.input.SheetXMLExecutor;
import com.rongji.egov.journal.service.utils.FileOperator;
import com.rongji.egov.mybatis.base.mapper.BaseMapper;
import com.rongji.egov.mybatis.base.pattern.verifier.SQLVerifier;
import com.rongji.egov.mybatis.base.sql.SQLInserter;
import com.rongji.egov.mybatis.base.utils.AutoCloseableBase;
import com.rongji.egov.mybatis.base.utils.StringUtils;
import com.rongji.egov.mybatis.dac.handler.Acl;
import com.rongji.egov.mybatis.dac.querier.DacUpdateQuerier;
import com.rongji.egov.mybatis.web.model.ModelLoader;
import org.apache.poi.openxml4j.opc.PackageAccess;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.util.HashSet;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("${spring.application.name}")
public class ImportExcelController {
    @Resource
    BaseMapper baseMapper;
    @Resource
    ModelLoader modelLoader;

    @Value("${rongji.module.journal.excel-path}")
    String root;

    public Acl checkAuthor() {
        Acl acl = modelLoader.getAcl();
        if (SQLVerifier.requireNonEmpty(acl.getRoleNoList()) || SQLVerifier.requireNonEmpty(acl.getRoleNoList())) {
            throw new RuntimeException("no authority .");
        }
        return acl;
    }

    @PostMapping("/excel/input")
    public Object excelInput(@RequestBody ImportConfig config,
                             @RequestParam(value = "file", required = false, defaultValue = "") String file) {
        Acl acl = checkAuthor();

        File dest = new File(root);
        if (!(dest.exists() && dest.isDirectory())) {
            return true;
        }
        SheetXMLExecutor sheetXMLExecutor = null;
        try {
            ImportExecutor executor = new ImportExecutor(config);
            sheetXMLExecutor = new SheetXMLExecutor(new File(dest.getAbsolutePath() + "/" + file.replaceAll(".*[/\\\\]", "")), PackageAccess.READ);
            return executor.action(sheetXMLExecutor, (model, values) ->
                    baseMapper.update(
                            modelLoader.invokeInterceptor(new DacUpdateQuerier().setAcl(acl)
                                    .setSqlHandler(new SQLInserter(model, values)))
                    )
            );
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            AutoCloseableBase.close(sheetXMLExecutor);
        }
    }

    @SuppressWarnings("all")
    @GetMapping("/excel/list")
    public String[] getExcelList() {
        checkAuthor();

        try {
            File file = new File(root);
            if (!(file.exists() && file.isDirectory())) {
                file.mkdirs();
            }
            return file.list();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    @SuppressWarnings("all")
    @PostMapping("/excel/del")
    public boolean getExcelList(@RequestBody HashSet<String> list) {
        checkAuthor();

        try {
            File file = new File(root);
            if (!(file.exists() && file.isDirectory())) {
                return true;
            }
            String base = file.getAbsolutePath() + "/";
            for (String path : list) {
                if (StringUtils.isBlank(path))   //path=path.replaceAll(".*[/\\\\]","")
                    continue;
                if (FileOperator.isInValidFileName(path)) {
                    throw new RuntimeException("file name is invalid , contains special character . " + path);
                }
                FileOperator.deleteDir(new File(base + path));
            }
            return true;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @GetMapping("/excel/download")
    public void downloadExcel(HttpServletResponse response,
                              @RequestParam(value = "file", required = false, defaultValue = "") String file) {
        checkAuthor();

        OutputStream os = null;
        InputStream is = null;
        try {
            file = file.replaceAll(".*[/\\\\]", "");
            File dest = new File(root);
            if (!(dest.exists() && dest.isDirectory())
                    || StringUtils.isBlank(file)
                    || !((dest = new File(dest.getAbsolutePath() + "/" + file)).exists() && dest.isFile())
            ) {
                throw new RuntimeException("can not find file : " + file);
            }

            response.setCharacterEncoding("UTF-8");
            response.setContentType("application/octet-stream");
            response.setContentLengthLong(dest.length());
            response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(file, "UTF-8"));
            FileOperator.copyStream(is = new FileInputStream(dest), os = response.getOutputStream());
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            AutoCloseableBase.close(is, os);
        }
    }

    @SuppressWarnings("all")
    @PostMapping("/excel/upload")
    public boolean uploadExcel(HttpServletRequest request) {
        checkAuthor();

        for (MultipartFile file : ((MultipartHttpServletRequest) request).getFiles("file")) {
            if (file.isEmpty()) {
                continue;   //throw new RuntimeException("file is empty .");
            }
            String filename = file.getOriginalFilename();
            if (StringUtils.isBlank(filename) || StringUtils.isBlank(filename = FileOperator.getFileNameByRegex(filename))) {
                throw new RuntimeException("file name is invalid .");
            }
            File dest = new File(root);
            if (!(dest.exists() && dest.isDirectory())) {
                dest.mkdirs();
            }
            dest = new File(dest.getAbsolutePath() + "/" + filename);
            try {
                file.transferTo(dest);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return true;
    }
}
