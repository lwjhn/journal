package com.rongji.egov.journal.service.model;

import com.rongji.egov.journal.service.base.filler.GenericFormFilter;
import com.rongji.egov.mybatis.base.annotation.*;
import com.rongji.egov.mybatis.base.wrapper.JsonTypeHandler;

import java.util.Set;

@SuppressWarnings("unused")
@Table(value = "EGOV_JOURNAL_ORDER", dac = true, mapping = Mapping.UNDERSCORE)
public class Order extends GenericFormFilter {
    private String pid;

    private String paperId;

    private Integer subscribeCopies;

    private Integer sortNo;

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

    public Set<String> getReaders() {
        return readers;
    }

    public void setReaders(Set<String> readers) {
        this.readers = readers;
    }
}