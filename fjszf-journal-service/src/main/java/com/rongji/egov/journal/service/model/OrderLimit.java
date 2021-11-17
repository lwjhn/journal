package com.rongji.egov.journal.service.model;

import com.rongji.egov.journal.service.base.model.GenericForm;
import com.rongji.egov.mybatis.base.annotation.*;
import com.rongji.egov.mybatis.base.wrapper.JsonTypeHandler;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Set;

@SuppressWarnings("unused")
@Table(value = "EGOV_JOURNAL_ORDER_LIMIT", dac = true, mapping = Mapping.UNDERSCORE)
public class OrderLimit extends GenericForm {
    private Integer subscribeYear;

    private Date subscribeBegin;

    private Date subscribeEnd;

    private Integer limitCount;

    private Integer limitCopies;

    private BigDecimal limitAmount;

    private String company;

    private Integer sortNo;

    private Boolean isValid;

    private Boolean requisite;

    private int repeatVerify;

    @Reader(ACL.PUB)
    @TypeHandler(JsonTypeHandler.class)
    private Set<String> readers;

    public Integer getSubscribeYear() {
        return subscribeYear;
    }

    public void setSubscribeYear(Integer subscribeYear) {
        this.subscribeYear = subscribeYear;
    }

    public Date getSubscribeBegin() {
        return subscribeBegin;
    }

    public void setSubscribeBegin(Date subscribeBegin) {
        this.subscribeBegin = subscribeBegin;
    }

    public Date getSubscribeEnd() {
        return subscribeEnd;
    }

    public void setSubscribeEnd(Date subscribeEnd) {
        this.subscribeEnd = subscribeEnd;
    }

    public Integer getLimitCount() {
        return limitCount;
    }

    public void setLimitCount(Integer limitCount) {
        this.limitCount = limitCount;
    }

    public Integer getLimitCopies() {
        return limitCopies;
    }

    public void setLimitCopies(Integer limitCopies) {
        this.limitCopies = limitCopies;
    }

    public BigDecimal getLimitAmount() {
        return limitAmount;
    }

    public void setLimitAmount(BigDecimal limitAmount) {
        this.limitAmount = limitAmount;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public Integer getSortNo() {
        return sortNo;
    }

    public void setSortNo(Integer sortNo) {
        this.sortNo = sortNo;
    }

    public Boolean getValid() {
        return isValid;
    }

    public void setValid(Boolean valid) {
        isValid = valid;
    }

    public Boolean getRequisite() {
        return requisite;
    }

    public void setRequisite(Boolean requisite) {
        this.requisite = requisite;
    }

    public int getRepeatVerify() {
        return repeatVerify;
    }

    public void setRepeatVerify(int repeatVerify) {
        this.repeatVerify = repeatVerify;
    }

    public Set<String> getReaders() {
        return readers;
    }

    public void setReaders(Set<String> readers) {
        this.readers = readers;
    }
}