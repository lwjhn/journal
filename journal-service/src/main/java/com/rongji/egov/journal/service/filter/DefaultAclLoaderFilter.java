package com.rongji.egov.journal.service.filter;

import com.rongji.egov.journal.service.properties.ModuleProperties;
import com.rongji.egov.mybatis.base.pattern.verifier.SQLVerifier;
import com.rongji.egov.mybatis.dac.handler.Acl;
import com.rongji.egov.mybatis.web.interceptor.AclLoaderFilter;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.HashSet;
import java.util.regex.Pattern;

@Component
public class DefaultAclLoaderFilter implements AclLoaderFilter {
    private final Pattern ROLE_PATTERN = Pattern.compile("^(journal_.+|sys_manager$)", Pattern.CASE_INSENSITIVE);
    private final Pattern GROUP_PATTERN = Pattern.compile(".+_journal$", Pattern.CASE_INSENSITIVE);
    private final HashSet<String> managers;

    public DefaultAclLoaderFilter(ObjectProvider<ModuleProperties> moduleProperties) {
        ModuleProperties properties = moduleProperties.getIfAvailable();
        managers = properties == null ? null : properties.managers;
    }

    @Override
    public Acl resolve(Acl acl) {
        return acl == null ? null : process(acl);
    }

    private Acl process(Acl acl) {
        process(acl.getGroupNoList(), GROUP_PATTERN);
        process(acl.getRoleNoList(), ROLE_PATTERN);
        return acl;
    }

    private void process(Collection<String> collection, Pattern pattern) {
        if (SQLVerifier.requireEmpty(collection))
            return;
        collection.removeIf(s -> managers == null ? !pattern.matcher(s).matches() : !managers.contains(s));
    }
}
