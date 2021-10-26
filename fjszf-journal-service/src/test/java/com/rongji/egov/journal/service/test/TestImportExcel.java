package com.rongji.egov.journal.service.test;

import com.alibaba.fastjson.JSONObject;
import com.rongji.egov.journal.service.excel.input.ImportConfig;
import com.rongji.egov.journal.service.excel.input.ImportExecutor;
import com.rongji.egov.journal.service.utils.FileOperator;
import com.rongji.egov.mybatis.base.mapper.BaseMapper;
import com.rongji.egov.mybatis.base.pattern.SQLFactory;
import com.rongji.egov.mybatis.base.sql.SQLInserter;
import com.rongji.egov.mybatis.base.utils.AutoCloseableBase;
import com.rongji.egov.mybatis.dac.querier.DacUpdateQuerier;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

@SpringBootTest
@RunWith(SpringRunner.class)
public class TestImportExcel {
    @Resource
    BaseMapper baseMapper;

    @Test
    public void test() {
        InputStream is = null;
        try {
            is = getClass().getResourceAsStream("/excel-import-config.json");
            assert is != null;
            ImportConfig config = JSONObject.parseObject(is, ImportConfig.class);
            AutoCloseableBase.close(is);
            ImportExecutor executor = new ImportExecutor(config);

            is = getClass().getResourceAsStream("/报刊产品查询.xlsx");
            assert is != null;
            int result = executor.action(new XSSFWorkbook(is), (model, values) -> {
                        System.out.println(SQLFactory.generate(new SQLInserter(model, values)));
                        return baseMapper.update(new DacUpdateQuerier().setAcl(null)
                                .setSqlHandler(new SQLInserter(model, values)));
                    }
            );
            System.out.println(result);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            AutoCloseableBase.close(is);
        }
    }
    @Value("${rongji.module.journal.excel-path}")
    String root;
    @Test
    public void test1() throws Exception {
        System.out.println(FileOperator.isInValidFileName("1634720205>317.xls"));
        System.out.println(FileOperator.isInValidFileName("1634720205317.xls"));
        System.out.println(getClass().getClassLoader().getResource("").getPath());
        System.out.println((new File(root)).getCanonicalPath());
    }
}
