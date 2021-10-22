package com.rongji.egov.journal.service.controller;

import com.rongji.egov.journal.service.excel.input.ImportConfig;
import com.rongji.egov.journal.service.excel.input.ImportExecutor;
import com.rongji.egov.mybatis.base.mapper.BaseMapper;
import com.rongji.egov.mybatis.base.sql.SQLInserter;
import com.rongji.egov.mybatis.base.utils.AutoCloseableBase;
import com.rongji.egov.mybatis.dac.handler.Acl;
import com.rongji.egov.mybatis.dac.querier.DacUpdateQuerier;
import com.rongji.egov.mybatis.web.model.ModelLoader;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.io.InputStream;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("${spring.application.name}")
public class ImportExcelController {
    @Resource
    BaseMapper baseMapper;

    @Resource
    ModelLoader modelLoader;

    @PostMapping("/excel/input")
    public Object queryPage(@RequestBody ImportConfig config,
                            @RequestParam(value = "file", required = false, defaultValue = "") String file) {
        Acl acl = modelLoader.getAcl();
        if (acl.getRoleNoList().size() < 1) {
            throw new RuntimeException("no authority .");
        }

        InputStream is = null;
        try {
            ImportExecutor executor = new ImportExecutor(config);
            is = getClass().getResourceAsStream("/" + file.replaceAll(".*[/\\\\]",""));
            assert is != null;
            return executor.action(new XSSFWorkbook(is), (model, values) ->
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
}
