package com.rongji.egov.journal.service.test;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.rongji.egov.journal.service.properties.ModuleProperties;
import com.rongji.egov.mybatis.base.mapper.BaseMapper;
import com.rongji.egov.mybatis.base.pattern.SQLFactory;
import com.rongji.egov.mybatis.base.querier.SelectListQuerier;
import com.rongji.egov.mybatis.base.sql.SQLSelector;
import com.rongji.egov.mybatis.base.utils.AutoCloseableBase;
import com.rongji.egov.mybatis.base.wrapper.HashCamelMap;
import com.rongji.egov.mybatis.dac.handler.Acl;
import com.rongji.egov.mybatis.dac.querier.DacSelectListQuerier;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import javax.validation.constraints.NotEmpty;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.regex.Pattern;

@SpringBootTest
@RunWith(SpringRunner.class)
public class TestService {
    @Resource
    BaseMapper baseMapper;

    @Resource
    ModuleProperties moduleProperties;

    Pattern pattern = Pattern.compile("(\\s|.)*<retmesg(\\s[^>]*)*>\\s*|\\s*</(\\s[^>]*)*retmesg(\\s[^>]*)*>(\\s|.)*", Pattern.CASE_INSENSITIVE);

    @Test
    public void testURL(){
        HttpURLConnection http = null;
        InputStream isr = null, ish=null;
        OutputStream os = null;
        try {
            http = (HttpURLConnection) (new URL("http://192.168.210.160/domcfg.nsf/testls?openAgent")).openConnection();
            http.setConnectTimeout(30000);
            http.setReadTimeout(30000);
            http.setDoInput(true);
            http.setDoOutput(true);
            http.setUseCaches(false);
            http.setRequestMethod("GET");
            http.setRequestProperty("Connection", "Close");
            os = http.getOutputStream();
            os.close();
            ish = http.getInputStream();
            byte[] buff = new byte[1024];
            int rc;
            os = System.out;
            while ((rc = ish.read(buff, 0, 1024)) > 0) {
                os.write(buff, 0, rc);
                os.flush();
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            AutoCloseableBase.close(isr, ish, os);
            try {
                if (http != null) http.disconnect();
            } catch (Exception ignored) {
            }
        }
    }

    @Test
    public void testProperty(){
        String src="<response_info>\n" +
                "        <gwid>486f8fe2-d49f-4570-9f86-c4ad00780613</gwid>\n" +
                "        <retcode >00</retcode>\n" +
                "        <retmesg s=\"1\">OK</ retmesg >\n" +
                "    </response_info>";
        System.out.println(src.replaceAll("(\\s|.)*<retmesg(\\s[^>]*)*>\\s*|\\s*</(\\s[^>]*)*retmesg(\\s[^>]*)*>(\\s|.)*",""));
        System.out.println(moduleProperties.managers.size());
        System.out.println(JSON.toJSONString(moduleProperties.managers));
    }

    @Test
    public void testGenerateSQL() {
        InputStream is = null;
        try {
            is = getClass().getClassLoader().getResourceAsStream("select-example-statistic.json");
            assert is != null;
            SQLSelector selector = JSONObject.parseObject(is, SQLSelector.class);
            System.out.println(SQLFactory.generate(selector));
            System.out.println("\n%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%\n");
            List<HashCamelMap> result = baseMapper.select(
                    new SelectListQuerier<HashCamelMap>().setResultMap(HashCamelMap.class).setSqlHandler(selector)
            );
            System.out.println(JSON.toJSONString(result));
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            AutoCloseableBase.close(is);
        }
    }


    @Test
    public void test3() {
        InputStream is = null;
        try {
            is = TestService.class.getClassLoader().getResourceAsStream("select-example.json");
            assert is != null;
            SQLSelector selector = JSONObject.parseObject(is, SQLSelector.class);
            List<HashCamelMap> result = baseMapper.select(
                    new DacSelectListQuerier<HashCamelMap>().setAcl(getAcl()).setResultMap(HashCamelMap.class).setSqlHandler(selector)
            );
            System.out.println(JSON.toJSONString(result));
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            AutoCloseableBase.close(is);
        }
    }

    @Test
    public void test4() {
        InputStream is = null;
        try {
            is = TestService.class.getClassLoader().getResourceAsStream("select-example-paper.json");
            assert is != null;
            SQLSelector selector = JSONObject.parseObject(is, SQLSelector.class);
            System.out.println(SQLFactory.generate(selector));
            List<HashCamelMap> result = baseMapper.select(
                    new SelectListQuerier<HashCamelMap>().setResultMap(HashCamelMap.class).setSqlHandler(selector)
            );
            System.out.println(JSON.toJSONString(result));
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            AutoCloseableBase.close(is);
        }
    }

    Acl getAcl() {
        return (new Acl() {
            @Override
            public @NotEmpty String getUserNo() {
                return "U000007";
            }

            @Override
            public Collection<String> getOrgNoList() {
                return new ArrayList<String>() {{
                    add("D00001");
                    add("D00005");
                    add("D00024");
                }};
            }

            @Override
            public Collection<String> getRoleNoList() {
                return new ArrayList<String>() {{
                    add("sys_manager");
                    add("D00005_csfzr");
                }};
            }

            @Override
            public Collection<String> getGroupNoList() {
                return new ArrayList<String>() {{
                    add("D00003_csgly");
                    add("D00005_csfzr");
                }};
            }
        });
    }
}
