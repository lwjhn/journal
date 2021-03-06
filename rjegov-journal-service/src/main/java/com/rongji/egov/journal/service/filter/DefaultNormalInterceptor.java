package com.rongji.egov.journal.service.filter;

import com.rongji.egov.journal.service.filter.model.*;
import com.rongji.egov.journal.service.model.*;
import com.rongji.egov.journal.service.properties.ModuleProperties;
import com.rongji.egov.mybatis.dac.handler.AclBaseQueryHandler;
import com.rongji.egov.mybatis.web.WebServiceProperties;
import com.rongji.egov.mybatis.web.interceptor.NormalInterceptor;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.HashSet;

@Component
public class DefaultNormalInterceptor implements NormalInterceptor {
    private final HashMap<Class<?>, NormalInterceptor> proxy = new HashMap<>();

    public DefaultNormalInterceptor(ObjectProvider<ModuleProperties> moduleProperties) {
        ModuleProperties properties = moduleProperties.getIfAvailable();
        HashSet<String> managers = properties == null ? null : properties.managers;
        proxy.put(Paper.class, new PaperInterceptor(managers));
        proxy.put(Subscription.class, new SubscriptionInterceptor(managers));
        proxy.put(Order.class, new OrderInterceptor(managers));
        proxy.put(StatPrintConfig.class, new StatPrintConfigInterceptor(managers));
        proxy.put(OrderLimit.class, new OrderLimitInterceptor(managers));
        proxy.put(DbConfig.class, new DbConfigInterceptor(managers));
    }

    @Override
    public AclBaseQueryHandler<?, ?> resolve(AclBaseQueryHandler<?, ?> aclBaseQueryHandler, WebServiceProperties webServiceProperties) {
        NormalInterceptor interceptor = proxy.get(aclBaseQueryHandler.getSqlHandler().getModel());
        return interceptor == null ? aclBaseQueryHandler
                : interceptor.resolve(aclBaseQueryHandler, webServiceProperties);
    }

    @Override
    public Object response(Object result, AclBaseQueryHandler<?, ?> handle, WebServiceProperties webServiceProperties) {
        NormalInterceptor interceptor = proxy.get(handle.getSqlHandler().getModel());
        return interceptor == null ? handle
                : interceptor.response(result, handle, webServiceProperties);
    }
}
