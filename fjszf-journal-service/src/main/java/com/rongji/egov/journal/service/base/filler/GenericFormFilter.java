package com.rongji.egov.journal.service.base.filler;

import com.rongji.egov.journal.service.base.model.GenericForm;
import com.rongji.egov.mybatis.base.builder.assistant.LambdaHelper;
import com.rongji.egov.mybatis.web.dac.RjAcl;

import java.util.*;
import java.util.function.Function;

@SuppressWarnings("unused")
public class GenericFormFilter {
    private final HashMap<String, Function<RjAcl, Object>> inserts = new HashMap<String, Function<RjAcl, Object>>() {{
        put(LambdaHelper.fieldName(GenericForm::getCreateTime), o -> new Date());
        put(LambdaHelper.fieldName(GenericForm::getId), o -> UUID.randomUUID().toString().replaceAll("-", ""));
        put(LambdaHelper.fieldName(GenericForm::getSystemNo), RjAcl::getSystemNo);
        put(LambdaHelper.fieldName(GenericForm::getDraftUser), RjAcl::getUserName);
        put(LambdaHelper.fieldName(GenericForm::getDraftUserNo), RjAcl::getUserNo);
        put(LambdaHelper.fieldName(GenericForm::getDraftOrg), RjAcl::getOrgName);
        put(LambdaHelper.fieldName(GenericForm::getDraftOrgNo), RjAcl::getOrgNo);
    }};
    private final HashMap<String, Function<RjAcl, Object>> updates = new HashMap<String, Function<RjAcl, Object>>() {{
        put(LambdaHelper.fieldName(GenericForm::getUpdateTime), o -> new Date());
    }};

    public HashMap<String, Function<RjAcl, Object>> getInserts() {
        return inserts;
    }

    public HashMap<String, Function<RjAcl, Object>> getUpdates() {
        return updates;
    }

    public void fillInsertsValues(Map<String, Object> values, RjAcl acl) {
        fillFixedValues(values, inserts.entrySet(), acl);
        fillFixedValues(values, updates.entrySet(), acl);
    }

    public void fillUpdatesValues(Map<String, Object> values, RjAcl acl) {
        for (String key : inserts.keySet()) {
            values.remove(key);
        }
        fillFixedValues(values, updates.entrySet(), acl);
    }

    protected void fillFixedValues(Map<String, Object> values, Set<Map.Entry<String, Function<RjAcl, Object>>> entrySet, RjAcl acl) {
        for (Map.Entry<String, Function<RjAcl, Object>> entry : entrySet) {
            values.put(entry.getKey(), entry.getValue().apply(acl));
        }
    }
}
