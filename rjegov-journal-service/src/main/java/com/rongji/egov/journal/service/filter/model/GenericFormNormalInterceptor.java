package com.rongji.egov.journal.service.filter.model;

import com.rongji.egov.journal.service.base.filler.GenericFormFilter;
import com.rongji.egov.journal.service.base.model.GenericForm;
import com.rongji.egov.mybatis.base.builder.assistant.LambdaHelper;
import com.rongji.egov.mybatis.base.builder.assistant.function.BiConsumer;
import com.rongji.egov.mybatis.base.sql.SQLHandler;
import com.rongji.egov.mybatis.base.sql.SQLInserter;
import com.rongji.egov.mybatis.base.sql.SQLUpdater;
import com.rongji.egov.mybatis.base.utils.ModelUtils;
import com.rongji.egov.mybatis.dac.handler.AclBaseQueryHandler;
import com.rongji.egov.mybatis.dac.querier.DacUpdateQuerier;
import com.rongji.egov.mybatis.web.WebServiceProperties;
import com.rongji.egov.mybatis.web.dac.RjAcl;
import com.rongji.egov.mybatis.web.interceptor.NormalInterceptor;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

@SuppressWarnings("unused")
public class GenericFormNormalInterceptor implements NormalInterceptor {
    protected final HashSet<String> managers;
    protected final GenericFormFilter genericFormFilter = new GenericFormFilter();
    private final HashMap<Class<?>, BiConsumer<GenericFormFilter, Map<String, Object>, RjAcl>> switcher =
            new HashMap<Class<?>, BiConsumer<GenericFormFilter, Map<String, Object>, RjAcl>>() {{
                put(SQLInserter.class, GenericFormFilter::fillInsertsValues);
                put(SQLUpdater.class, GenericFormFilter::fillUpdatesValues);
            }};

    public GenericFormNormalInterceptor(HashSet<String> managers) {
        this.managers = managers;
        genericFormFilter.getInserts().put(LambdaHelper.fieldName(GenericForm::getManagers), o -> managers);
    }

    @SuppressWarnings("unchecked")
    public AclBaseQueryHandler<?, ?> action(DacUpdateQuerier updateQuerier, WebServiceProperties webServiceProperties) {
        SQLHandler sqlHandler = updateQuerier.getSqlHandler();
        if (sqlHandler instanceof SQLInserter && ModelUtils.isSubclass(sqlHandler.getModel(), GenericForm.class)) {
            BiConsumer<GenericFormFilter, Map<String, Object>, RjAcl> handle = switcher.get(sqlHandler.getClass());
            if (handle != null) {
                handle.accept(genericFormFilter, (Map<String, Object>) ((SQLInserter) sqlHandler).getValues(), (RjAcl) updateQuerier.getAcl());
            }
        }
        return updateQuerier;
    }

    @Override
    public AclBaseQueryHandler<?, ?> resolve(AclBaseQueryHandler<?, ?> aclBaseQueryHandler, WebServiceProperties webServiceProperties) {
        return aclBaseQueryHandler instanceof DacUpdateQuerier
                ? action((DacUpdateQuerier) aclBaseQueryHandler, webServiceProperties) : aclBaseQueryHandler;
    }

    @SuppressWarnings("unchecked")
    @Override
    public Object response(Object result, AclBaseQueryHandler<?, ?> handle, WebServiceProperties webServiceProperties) {
        if (handle instanceof DacUpdateQuerier) {
            SQLHandler sqlHandler = handle.getSqlHandler();
            if (sqlHandler.getClass() == SQLInserter.class && ModelUtils.isSubclass(sqlHandler.getModel(), GenericForm.class) && ((int) result) == 1) {
                return ((Map<String, Object>) ((SQLInserter) sqlHandler).getValues()).get(LambdaHelper.fieldName(GenericForm::getId));
            }
        }
        return result;
    }
}
