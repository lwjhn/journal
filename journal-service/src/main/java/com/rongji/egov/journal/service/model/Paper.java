package com.rongji.egov.journal.service.model;

import com.rongji.egov.journal.service.base.model.GenericForm;
import com.rongji.egov.mybatis.base.annotation.*;
import com.rongji.egov.mybatis.base.wrapper.JsonTypeHandler;

import java.math.BigDecimal;
import java.util.Set;

@SuppressWarnings("unused")
@Table(value = "EGOV_JOURNAL_SUBSCRIPTION", dac = true, mapping = Mapping.UNDERSCORE)
public class Paper extends GenericForm {
    private String publication;

    private String postalDisCode;

    private String journal;

    private String lang;

    private String paperType;

    private String periodical;

    private BigDecimal unitPrice;

    private BigDecimal yearPrice;

    private String deliveryMethod;

    private String barcode;

    private String press;

    private String phone;

    private String pressAddress;

    private String programa;

    private String presentation;

    private Boolean govExpense;

    private Boolean isValid;

    private Integer sortNo;

    @Reader(ACL.PUB)
    @TypeHandler(JsonTypeHandler.class)
    private Set<String> readers;

    public String getPublication() {
        return publication;
    }

    public void setPublication(String publication) {
        this.publication = publication;
    }

    public String getPostalDisCode() {
        return postalDisCode;
    }

    public void setPostalDisCode(String postalDisCode) {
        this.postalDisCode = postalDisCode;
    }

    public String getJournal() {
        return journal;
    }

    public void setJournal(String journal) {
        this.journal = journal;
    }

    public String getLang() {
        return lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }

    public String getPaperType() {
        return paperType;
    }

    public void setPaperType(String paperType) {
        this.paperType = paperType;
    }

    public String getPeriodical() {
        return periodical;
    }

    public void setPeriodical(String periodical) {
        this.periodical = periodical;
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
    }

    public BigDecimal getYearPrice() {
        return yearPrice;
    }

    public void setYearPrice(BigDecimal yearPrice) {
        this.yearPrice = yearPrice;
    }

    public String getDeliveryMethod() {
        return deliveryMethod;
    }

    public void setDeliveryMethod(String deliveryMethod) {
        this.deliveryMethod = deliveryMethod;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public String getPress() {
        return press;
    }

    public void setPress(String press) {
        this.press = press;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPressAddress() {
        return pressAddress;
    }

    public void setPressAddress(String pressAddress) {
        this.pressAddress = pressAddress;
    }

    public String getPrograma() {
        return programa;
    }

    public void setPrograma(String programa) {
        this.programa = programa;
    }

    public String getPresentation() {
        return presentation;
    }

    public void setPresentation(String presentation) {
        this.presentation = presentation;
    }

    public Boolean getGovExpense() {
        return govExpense;
    }

    public void setGovExpense(Boolean govExpense) {
        this.govExpense = govExpense;
    }

    public Boolean getValid() {
        return isValid;
    }

    public void setValid(Boolean valid) {
        isValid = valid;
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