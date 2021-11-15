package com.rongji.egov.journal.service.filter.model;

import com.rongji.egov.journal.service.model.Paper;
import com.rongji.egov.mybatis.base.builder.assistant.LambdaHelper;
import com.rongji.egov.mybatis.web.dac.RjAcl;

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
}
