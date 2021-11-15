package com.rongji.egov.journal.service.model;

import com.rongji.egov.journal.service.base.model.GenericForm;
import com.rongji.egov.mybatis.base.annotation.*;
import com.rongji.egov.mybatis.base.wrapper.JsonTypeHandler;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

@SuppressWarnings("unused")
@Table(value = "EGOV_JOURNAL_DB_CONFIG", dac = true, mapping = Mapping.UNDERSCORE)
public class DbConfig extends GenericForm {
    private String panelUrl;
    private int panelHorizontal;
    private int panelVertical;

    @TypeHandler(JsonTypeHandler.class)
    private List<Map<String, Object>> panelItems;
    @Reader(ACL.PUB)
    @TypeHandler(JsonTypeHandler.class)
    private Set<String> readers;

    public String getPanelUrl() {
        return panelUrl;
    }

    public void setPanelUrl(String panelUrl) {
        this.panelUrl = panelUrl;
    }

    public int getPanelHorizontal() {
        return panelHorizontal;
    }

    public void setPanelHorizontal(int panelHorizontal) {
        this.panelHorizontal = panelHorizontal;
    }

    public int getPanelVertical() {
        return panelVertical;
    }

    public void setPanelVertical(int panelVertical) {
        this.panelVertical = panelVertical;
    }

    public List<Map<String, Object>> getPanelItems() {
        return panelItems;
    }

    public void setPanelItems(List<Map<String, Object>> panelItems) {
        this.panelItems = panelItems;
    }

    public Set<String> getReaders() {
        return readers;
    }

    public void setReaders(Set<String> readers) {
        this.readers = readers;
    }
}