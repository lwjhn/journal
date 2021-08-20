package com.rongji.egov.journal.service.filter.model;

import com.rongji.egov.journal.service.model.Subscription;
import com.rongji.egov.mybatis.base.builder.SQLCriteriaCollector;
import com.rongji.egov.mybatis.base.builder.assistant.LambdaHelper;
import com.rongji.egov.mybatis.base.sql.SQLCriteria;
import com.rongji.egov.mybatis.base.sql.SQLDeleter;
import com.rongji.egov.mybatis.base.sql.SQLInserter;
import com.rongji.egov.mybatis.base.sql.SQLSimpleHandler;
import com.rongji.egov.mybatis.dac.handler.AclBaseQueryHandler;
import com.rongji.egov.mybatis.web.WebServiceProperties;
import com.rongji.egov.mybatis.web.dac.RjAcl;
import org.springframework.util.Assert;

import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.function.BiFunction;

public class SubscriptionInterceptor extends GenericFormNormalInterceptor {
    private final HashMap<String, BiFunction<RjAcl, Integer, Object>> verifier = new HashMap<String, BiFunction<RjAcl, Integer, Object>>() {{
        put(LambdaHelper.fieldName(Subscription::getVerifyStatus), (acl, status) -> status);
        put(LambdaHelper.fieldName(Subscription::getVerifyTime), (acl, status) -> status == 2 ? new Date() : null);
        put(LambdaHelper.fieldName(Subscription::getVerifyUserNo), (acl, status) -> status == 2 ? acl.getUserNo() : null);
        put(LambdaHelper.fieldName(Subscription::getVerifyUser), (acl, status) -> status == 2 ? acl.getUserName() : null);
    }};

    private void verify(Map<String, Object> values, RjAcl acl) {
        Integer verifyStatus = (Integer) values.get(LambdaHelper.fieldName(Subscription::getVerifyStatus));
        if (verifyStatus == null || verifyStatus < 0 || verifyStatus > 2) {
            verifyStatus = 0;
        }
        if (verifyStatus == 2) {
            Assert.isTrue(acl.getRoleNoList().size() + acl.getGroupNoList().size() > 0, "not authorized to perform this operation (verifyStatus=2).");
        }
        for (Map.Entry<String, BiFunction<RjAcl, Integer, Object>> entry : verifier.entrySet()) {
            values.put(entry.getKey(), entry.getValue().apply(acl, verifyStatus));
        }
    }

    private final SQLCriteria criteriaDeleter = new SQLCriteria(LambdaHelper.fieldName(Subscription::getVerifyStatus) + " = ?", 0);

    public SubscriptionInterceptor(HashSet<String> managers) {
        super(managers);
    }

    @SuppressWarnings("unchecked")
    @Override
    public AclBaseQueryHandler<?, ?> resolve(AclBaseQueryHandler<?, ?> aclBaseQueryHandler, WebServiceProperties webServiceProperties) {
        SQLSimpleHandler sqlHandler = (SQLSimpleHandler) aclBaseQueryHandler.getSqlHandler();
        if (sqlHandler instanceof SQLDeleter) {
            sqlHandler.setWhere(new SQLCriteriaCollector().add(sqlHandler.getWhere()).add(criteriaDeleter).toSQLCriteria());
        } else if (sqlHandler instanceof SQLInserter) {
            verify((Map<String, Object>) ((SQLInserter) sqlHandler).getValues(), (RjAcl) aclBaseQueryHandler.getAcl());
        }
        return super.resolve(aclBaseQueryHandler, webServiceProperties);
    }
}
