package com.rongji.egov.journal.service.base.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("${spring.application.name}")
public class InitializedController {
//    @Resource
//    ModuleUtils moduleUtils;
//
//    @GetMapping("/initialized/initRecord")
//    public GenericForm initRecord() {
//        GenericForm record = new GenericForm();
//        moduleUtils.initUmsUser4Form(record);
//        return record;
//    }
//
//    @GetMapping("/initialized/isManager")
//    public boolean isManager(){
//        return moduleUtils.isManager(null);
//    }
}
