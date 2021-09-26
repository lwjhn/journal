package com.rongji.egov.journal.service.model;

import com.rongji.egov.journal.service.base.model.GenericForm;
import com.rongji.egov.mybatis.base.annotation.*;
import com.rongji.egov.mybatis.base.wrapper.JsonTypeHandler;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

@SuppressWarnings("unused")
@Table(value = "EGOV_JOURNAL_STAT_PRINT_CONFIG", dac = true, mapping = Mapping.UNDERSCORE)
public class StatPrintConfig extends GenericForm {
    private Integer sortNo;

    private String company;

    private String postalCode;

    private String transactor;

    private String phoneNo;

    private String address;

    @Reader(ACL.PUB)
    @TypeHandler(JsonTypeHandler.class)
    private Set<String> readers;

    public Integer getSortNo() {
        return sortNo;
    }

    public void setSortNo(Integer sortNo) {
        this.sortNo = sortNo;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getTransactor() {
        return transactor;
    }

    public void setTransactor(String transactor) {
        this.transactor = transactor;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Set<String> getReaders() {
        return readers;
    }

    public void setReaders(Set<String> readers) {
        this.readers = readers;
    }
}