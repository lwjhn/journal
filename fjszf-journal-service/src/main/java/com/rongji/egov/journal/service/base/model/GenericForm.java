package com.rongji.egov.journal.service.base.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.rongji.egov.mybatis.base.annotation.ACL;
import com.rongji.egov.mybatis.base.annotation.Editor;
import com.rongji.egov.mybatis.base.annotation.TypeHandler;
import com.rongji.egov.mybatis.base.wrapper.JsonTypeHandler;

import java.util.Date;
import java.util.Set;

@SuppressWarnings("unused")
public class GenericForm extends BaseForm {
    private String draftUser;

    @Editor(ACL.USER)
    private String draftUserNo;

    private String draftOrg;

    private String draftOrgNo;

    @Editor({ACL.GROUP, ACL.ROLE})
    @TypeHandler(JsonTypeHandler.class)
    private Set<String> managers;

    private String systemNo;

    @JsonFormat(locale = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    @JsonFormat(locale = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

    public String getDraftUser() {
        return draftUser;
    }

    public void setDraftUser(String draftUser) {
        this.draftUser = draftUser;
    }

    public String getDraftUserNo() {
        return draftUserNo;
    }

    public void setDraftUserNo(String draftUserNo) {
        this.draftUserNo = draftUserNo;
    }

    public String getDraftOrg() {
        return draftOrg;
    }

    public void setDraftOrg(String draftOrg) {
        this.draftOrg = draftOrg;
    }

    public String getDraftOrgNo() {
        return draftOrgNo;
    }

    public void setDraftOrgNo(String draftOrgNo) {
        this.draftOrgNo = draftOrgNo;
    }

    public String getSystemNo() {
        return systemNo;
    }

    public void setSystemNo(String systemNo) {
        this.systemNo = systemNo;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Set<String> getManagers() {
        return managers;
    }

    public void setManagers(Set<String> managers) {
        this.managers = managers;
    }
}
