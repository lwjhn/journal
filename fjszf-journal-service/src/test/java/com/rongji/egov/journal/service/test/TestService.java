package com.rongji.egov.journal.service.test;

import com.alibaba.fastjson.JSON;
import com.rongji.egov.journal.service.properties.ModuleProperties;
import com.rongji.egov.mybatis.base.mapper.BaseMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
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
    public void test1() {
        for(int i=0;i<6;i++){
            System.out.println(UUID.randomUUID().toString().replaceAll("-", ""));
        }
//        SQLInserter inserter = new SQLInserter(submitReport);
//        System.out.println(JSON.toJSONString(inserter, true));
//
//        System.out.println(baseMapper.update((new UpdateQuerier()).setSqlHandler(inserter)));
    }

    @Test
    public void test4() {
//        String id = "T3-1628779339612";
//
//        SQLUpdater updater = new SQLUpdater(submitReport, new SQLCriteria("id = ?", new ArrayList<Object>() {{
//            add(id);
//        }}));
//        System.out.println(JSON.toJSONString(updater, true));
//
//        System.out.println(baseMapper.update(new UpdateQuerier().setSqlHandler(updater)));
    }

    @Test
    public void testDelete() {
//        String id = "T3-1628779339612";
//        SQLDeleter handle = new SQLDeleter(SubmitReport.class, new SQLCriteria("id LIKE ?", id));
//        System.out.println(JSON.toJSONString(handle, true));
//        System.out.println(baseMapper.update(new UpdateQuerier().setSqlHandler(handle)));
    }

    @Test
    public void test3() {
//        SQLSelector selector = new SQLSelector(
//                new SQLCriteria("subject LIKE ?", "%测试%"), SubmitReport.class);
//        selector.setLimit(0, 6);
//        selector.setFields(SQLWrapper.getSqlFields(SubmitReport.class, true));
//
//        Page<HashCamelMap> test = baseMapper.select(new SelectSimpleQuerier<Page<HashCamelMap>>() {
//        }.setSqlHandler(selector));
//
//        Page<HashCamelMap> reportHashCamelMap = baseMapper.select(
//                new SelectPageQuerier<HashCamelMap>().setResultMap(HashCamelMap.class).setSqlHandler(selector));
//
//        selector.setFields(SQLWrapper.getSqlFields(SubmitReport.class));
//        Page<SubmitReport> reportPage = baseMapper.select(
//                new SelectPageQuerier<SubmitReport>()
//                        .setResultMap(SubmitReport.class).setSqlHandler(selector));
//
//        List<SubmitReport> reports = baseMapper.select(
//                new SelectListQuerier<SubmitReport>()
//                        .setResultMap(SubmitReport.class).setSqlHandler(selector));
//        FlowReaderList flowReaderList = reports.get(0).getTodoReader();
//
//        selector.setLimit(0, 1);
//        SubmitReport report = baseMapper.select(
//                new SelectOneQuerier<SubmitReport>()
//                        .setResultMap(SubmitReport.class).setSqlHandler(selector));
//        System.out.println(JSON.toJSONString(test));
    }
}
