package com.rongji.egov.journal.service.excel.input;

import com.rongji.egov.mybatis.base.sql.SQLCriteria;

import java.util.Map;

public class ImportConfig {
    private int sheet;
    private int beginRow;
    private int endRow;
    private Class<?> model;
    private Map<String, SQLCriteria> values;

    public int getSheet() {
        return sheet;
    }

    public void setSheet(int sheet) {
        this.sheet = sheet;
    }

    public int getBeginRow() {
        return beginRow;
    }

    public void setBeginRow(int beginRow) {
        this.beginRow = beginRow;
    }

    public int getEndRow() {
        return endRow;
    }

    public void setEndRow(int endRow) {
        this.endRow = endRow;
    }

    public Class<?> getModel() {
        return model;
    }

    public void setModel(Class<?> model) {
        this.model = model;
    }

    public Map<String, SQLCriteria> getValues() {
        return values;
    }

    public void setValues(Map<String, SQLCriteria> values) {
        this.values = values;
    }
}
