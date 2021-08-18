package com.rongji.egov.journal.service.filter.model;

import com.rongji.egov.journal.service.model.Subscription;
import com.rongji.egov.mybatis.base.builder.SQLCriteriaCollector;
import com.rongji.egov.mybatis.base.builder.assistant.LambdaHelper;
import com.rongji.egov.mybatis.base.sql.SQLCriteria;
import com.rongji.egov.mybatis.base.sql.SQLDeleter;
import com.rongji.egov.mybatis.base.sql.SQLSimpleHandler;
import com.rongji.egov.mybatis.dac.handler.AclBaseQueryHandler;
import com.rongji.egov.mybatis.web.WebServiceProperties;

import java.util.HashSet;

public class SubscriptionInterceptor extends GenericFormNormalInterceptor {
    private final SQLCriteria criteriaDeleter = new SQLCriteria(LambdaHelper.fieldName(Subscription::getVerifyStatus) + " = ?", 0);

    public SubscriptionInterceptor(HashSet<String> managers) {
        super(managers);
    }

    @Override
    public AclBaseQueryHandler<?, ?> resolve(AclBaseQueryHandler<?, ?> aclBaseQueryHandler, WebServiceProperties webServiceProperties) {
        SQLSimpleHandler sqlHandler = (SQLSimpleHandler) aclBaseQueryHandler.getSqlHandler();
        if (sqlHandler instanceof SQLDeleter) {
            sqlHandler.setWhere(new SQLCriteriaCollector().add(sqlHandler.getWhere()).add(criteriaDeleter).toSQLCriteria());
        }
        return super.resolve(aclBaseQueryHandler, webServiceProperties);
    }
}
