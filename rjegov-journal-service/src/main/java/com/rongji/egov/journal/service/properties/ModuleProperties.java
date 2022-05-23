package com.rongji.egov.journal.service.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.HashSet;

@ConfigurationProperties(prefix = ModuleProperties.PREFIX)
public class ModuleProperties {
    public static final String PREFIX = "rongji.module.journal";
    public HashSet<String> managers;
    public String excelPath;

    public HashSet<String> getManagers() {
        return managers;
    }

    public void setManagers(HashSet<String> managers) {
        this.managers = managers;
    }

    public String getExcelPath() {
        return excelPath;
    }

    public void setExcelPath(String excelPath) {
        this.excelPath = excelPath;
    }
}
