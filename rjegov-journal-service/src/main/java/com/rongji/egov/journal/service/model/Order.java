package com.rongji.egov.journal.service.model;

import com.rongji.egov.journal.service.base.model.GenericForm;
import com.rongji.egov.mybatis.base.annotation.*;
import com.rongji.egov.mybatis.base.wrapper.JsonTypeHandler;

import java.util.Set;

@SuppressWarnings("unused")
@Table(value = "EGOV_JOURNAL_ORDER", dac = true, mapping = Mapping.UNDERSCORE)
public class Order extends GenericForm {
    private String pid;

    private String paperId;

    private Integer subscribeCopies;

    private Integer sortNo;

    private Boolean dispatched;

    @Reader(ACL.PUB)
    @TypeHandler(JsonTypeHandler.class)
    private Set<String> readers;

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public Integer getSubscribeCopies() {
        return subscribeCopies;
    }

    public void setSubscribeCopies(Integer subscribeCopies) {
        this.subscribeCopies = subscribeCopies;
    }

    public Integer getSortNo() {
        return sortNo;
    }

    public void setSortNo(Integer sortNo) {
        this.sortNo = sortNo;
    }

    public String getPaperId() {
        return paperId;
    }

    public void setPaperId(String paperId) {
        this.paperId = paperId;
    }

    public Boolean getDispatched() {
        return dispatched;
    }

    public void setDispatched(Boolean dispatched) {
        this.dispatched = dispatched;
    }

    public Set<String> getReaders() {
        return readers;
    }

    public void setReaders(Set<String> readers) {
        this.readers = readers;
    }
}