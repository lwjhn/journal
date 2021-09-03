package com.rongji.egov.journal.service.test;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.rongji.egov.journal.service.properties.ModuleProperties;
import com.rongji.egov.mybatis.base.mapper.BaseMapper;
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
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

@SpringBootTest
@RunWith(SpringRunner.class)
public class TestService {
    @Resource
    BaseMapper baseMapper;

    @Resource
    ModuleProperties moduleProperties;

    @Test
    public void testProperty(){
        System.out.println(moduleProperties.managers.size());
        System.out.println(JSON.toJSONString(moduleProperties.managers));
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
            is = TestService.class.getClassLoader().getResourceAsStream("select-example.json");
            assert is != null;
            SQLSelector selector = JSONObject.parseObject(is, SQLSelector.class);
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
