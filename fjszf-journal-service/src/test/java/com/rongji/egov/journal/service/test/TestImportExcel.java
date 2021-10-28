package com.rongji.egov.journal.service.test;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.rongji.egov.journal.service.excel.input.ImportConfig;
import com.rongji.egov.journal.service.excel.input.ImportExecutor;
import com.rongji.egov.journal.service.excel.input.SheetXMLExecutor;
import com.rongji.egov.journal.service.utils.FileOperator;
import com.rongji.egov.mybatis.base.mapper.BaseMapper;
import com.rongji.egov.mybatis.base.pattern.SQLFactory;
import com.rongji.egov.mybatis.base.sql.SQLInserter;
import com.rongji.egov.mybatis.base.utils.AutoCloseableBase;
import com.rongji.egov.mybatis.dac.querier.DacUpdateQuerier;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.openxml4j.opc.PackageAccess;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.util.SAXHelper;
import org.apache.poi.xssf.eventusermodel.ReadOnlySharedStringsTable;
import org.apache.poi.xssf.eventusermodel.XSSFReader;
import org.apache.poi.xssf.eventusermodel.XSSFSheetXMLHandler;
import org.apache.poi.xssf.model.StylesTable;
import org.apache.poi.xssf.usermodel.XSSFComment;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.xml.sax.ContentHandler;
import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@SpringBootTest
@RunWith(SpringRunner.class)
public class TestImportExcel {
    @Resource
    BaseMapper baseMapper;

    @Test
    public void test() {
        InputStream is = null;
        SheetXMLExecutor sheetXMLExecutor = null;
        try {
            is = getClass().getResourceAsStream("/excel-import-config.json");
            assert is != null;
            ImportConfig config = JSONObject.parseObject(is, ImportConfig.class);
            AutoCloseableBase.close(is);
            ImportExecutor executor = new ImportExecutor(config);

            sheetXMLExecutor = new SheetXMLExecutor(new File("C:/temp/报刊产品查询.xlsx"), PackageAccess.READ);
            int result = executor.action(sheetXMLExecutor, (model, values) -> {
                        //System.out.println(JSON.toJSONString(new SQLInserter(model, values)));
                        //System.out.println(SQLFactory.generate(new SQLInserter(model, values)));
                        return baseMapper.update(new DacUpdateQuerier().setAcl(null)
                                .setSqlHandler(new SQLInserter(model, values)));
                    }
            );
            System.out.println(result);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            AutoCloseableBase.close(sheetXMLExecutor, is);
        }
    }

    @Test
    public void test3() {
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
        for (int i = -2; i < 100; i++) {
            String t = toTwentySix(i);
            System.out.printf("%s : %d \n", t, i);
        }

        System.out.println(FileOperator.isInValidFileName("1634720205>317.xls"));
        System.out.println(FileOperator.isInValidFileName("1634720205317.xls"));
        System.out.println(getClass().getClassLoader().getResource("").getPath());
        System.out.println((new File(root)).getCanonicalPath());
    }

    public static String toTwentySix(int n) {
        if (n == 0) {
            return "0";
        }
        if (n < 0) {
            return "-" + toTwentySix(-n);
        }
        n--;
        StringBuilder s = new StringBuilder();
        int offset = 0;
        do {
            int m = n % 26;
            s.insert(0, (char) (m + offset + 'A'));
            n = (n - m) / 26;
            offset = -1;
        } while (n > 0);
        return s.toString();
    }
}
