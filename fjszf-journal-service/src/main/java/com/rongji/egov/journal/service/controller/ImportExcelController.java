package com.rongji.egov.journal.service.controller;

import com.rongji.egov.journal.service.excel.input.ImportConfig;
import com.rongji.egov.journal.service.excel.input.ImportExecutor;
import com.rongji.egov.journal.service.utils.FileOperator;
import com.rongji.egov.mybatis.base.mapper.BaseMapper;
import com.rongji.egov.mybatis.base.sql.SQLInserter;
import com.rongji.egov.mybatis.base.utils.AutoCloseableBase;
import com.rongji.egov.mybatis.base.utils.StringUtils;
import com.rongji.egov.mybatis.dac.handler.Acl;
import com.rongji.egov.mybatis.dac.querier.DacUpdateQuerier;
import com.rongji.egov.mybatis.web.model.ModelLoader;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
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

    public Acl checkAuthor(){
        Acl acl = modelLoader.getAcl();
        if (acl.getRoleNoList().size() < 1) {
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
        InputStream is = null;
        try {
            ImportExecutor executor = new ImportExecutor(config);
            is = new FileInputStream(dest.getAbsolutePath() + "/" + file.replaceAll(".*[/\\\\]",""));
            Assert.notNull(is, "can't find file . " + file.replaceAll(".*[/\\\\]",""));
            System.out.println(">>>-loading-->" + file);
            XSSFWorkbook workbook=new XSSFWorkbook(is);
            System.out.println(">>>-loaded-->" + file);
            return executor.action(workbook, (model, values) ->
                    baseMapper.update(
                            modelLoader.invokeInterceptor(new DacUpdateQuerier().setAcl(acl)
                                    .setSqlHandler(new SQLInserter(model, values)))
                    )
            );
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            AutoCloseableBase.close(is);
        }
    }

    @SuppressWarnings("all")
    @GetMapping("/excel/list")
    public String[] getExcelList(){
        checkAuthor();

        try {
            File file = new File(root);
            if (!(file.exists() && file.isDirectory())) {
                file.mkdirs();
            }
            return file.list();
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }


    @SuppressWarnings("all")
    @PostMapping("/excel/del")
    public boolean getExcelList(@RequestBody HashSet<String> list){
        checkAuthor();

        try {
            File file = new File(root);
            if (!(file.exists() && file.isDirectory())) {
                return true;
            }
            String base = file.getAbsolutePath() + "/";
            for(String path : list){
                if(StringUtils.isBlank(path))
                    continue;
                if(FileOperator.isInValidFileName(path)){
                    throw new RuntimeException("file name is invalid , contains special character . " + path);
                }
                FileOperator.deleteDir(new File(base + path));
            }
            return true;
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    @SuppressWarnings("all")
    @PostMapping("/excel/upload")
    public boolean uploadExcel(HttpServletRequest request){
        checkAuthor();

        for(MultipartFile file : ((MultipartHttpServletRequest) request).getFiles("file")){
            if(file.isEmpty()){
                continue;   //throw new RuntimeException("file is empty .");
            }
            String filename = file.getOriginalFilename();
            if(StringUtils.isBlank(filename) || StringUtils.isBlank(filename=FileOperator.getFileNameByRegex(filename))){
                throw new RuntimeException("file name is invalid .");
            }
            File dest = new File(root);
            if (!(dest.exists() && dest.isDirectory())) {
                dest.mkdirs();
            }
            dest=new File(dest.getAbsolutePath() + "/" + filename);
            try{
                file.transferTo(dest);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return true;
    }
}
