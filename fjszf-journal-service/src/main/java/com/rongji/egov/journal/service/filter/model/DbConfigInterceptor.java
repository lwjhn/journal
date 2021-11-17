package com.rongji.egov.journal.service.filter.model;

import com.rongji.egov.journal.service.model.Paper;
import com.rongji.egov.mybatis.base.builder.assistant.LambdaHelper;
import com.rongji.egov.mybatis.base.pattern.verifier.SQLVerifier;
import com.rongji.egov.mybatis.base.sql.SQLDeleter;
import com.rongji.egov.mybatis.base.sql.SQLInserter;
import com.rongji.egov.mybatis.dac.handler.Acl;
import com.rongji.egov.mybatis.dac.handler.AclBaseQueryHandler;
import com.rongji.egov.mybatis.web.WebServiceProperties;
import com.rongji.egov.mybatis.web.dac.RjAcl;
import org.springframework.util.Assert;

import java.util.HashSet;
import java.util.Set;

public class DbConfigInterceptor extends GenericFormNormalInterceptor {
    private final static Set<String> readers = new HashSet<String>(){{
        add(RjAcl.FULL_ACCESS_NO);
    }};

    public DbConfigInterceptor(HashSet<String> managers) {
        super(managers);
        this.genericFormFilter.getInserts().put(LambdaHelper.fieldName(Paper::getReaders), o -> readers);
    }

    @Override
    public AclBaseQueryHandler<?, ?> resolve(AclBaseQueryHandler<?, ?> aclBaseQueryHandler, WebServiceProperties webServiceProperties) {
        Object sqlHandler = aclBaseQueryHandler.getSqlHandler();
        if (sqlHandler instanceof SQLDeleter || sqlHandler instanceof SQLInserter) {
            Acl acl = aclBaseQueryHandler.getAcl();
            Assert.isTrue(SQLVerifier.requireNonEmpty(acl.getRoleNoList()) || SQLVerifier.requireNonEmpty(acl.getRoleNoList()), "not authorized to perform this operation (verifyStatus=2).");
        }
        return super.resolve(aclBaseQueryHandler, webServiceProperties);
    }
}
