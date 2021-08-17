package com.rongji.egov.journal.service.filter.model;

import com.rongji.egov.mybatis.dac.handler.AclBaseQueryHandler;
import com.rongji.egov.mybatis.dac.querier.DacUpdateQuerier;
import com.rongji.egov.mybatis.web.WebServiceProperties;

import java.util.HashSet;

public class SubscriptionInterceptor extends GenericFormNormalInterceptor {

    public SubscriptionInterceptor(HashSet<String> managers) {
        super(managers);
    }

    @Override
    public AclBaseQueryHandler<?, ?> resolve(AclBaseQueryHandler<?, ?> aclBaseQueryHandler, WebServiceProperties webServiceProperties) {
        return aclBaseQueryHandler instanceof DacUpdateQuerier
                ? super.resolve(aclBaseQueryHandler, webServiceProperties) : aclBaseQueryHandler;
    }
}
