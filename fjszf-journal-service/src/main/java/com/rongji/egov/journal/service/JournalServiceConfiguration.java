package com.rongji.egov.journal.service;

import com.rongji.egov.app.support.AppSupportConfiguration;
import com.rongji.egov.journal.service.model.Subscription;
import com.rongji.egov.journal.service.properties.ModuleProperties;
import com.rongji.egov.mybatis.base.annotation.ModelScan;
import com.rongji.egov.mybatis.base.wrapper.IterableTypeHandler;
import com.rongji.egov.security.service.SecurityServiceConfiguration;
import com.rongji.egov.user.client.UserClientConfiguration;
import com.rongji.egov.utils.spring.configuration.ClientConfiguration;
import com.rongji.egov.utils.spring.configuration.IgnoredPathsConfiguration;
import com.rongji.egov.utils.spring.configuration.WebConfiguration;
import org.apache.ibatis.session.LocalCacheScope;
import org.apache.ibatis.type.JdbcType;
import org.mybatis.spring.boot.autoconfigure.ConfigurationCustomizer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@ModelScan(basePackageClasses = Subscription.class)
@EnableConfigurationProperties(ModuleProperties.class)
@SpringBootApplication
@EnableEurekaClient
@EnableTransactionManagement
@Import({WebConfiguration.class,
        SecurityServiceConfiguration.class,
        ClientConfiguration.class,
        UserClientConfiguration.class,
        AppSupportConfiguration.class,
        IgnoredPathsConfiguration.class
})
public class JournalServiceConfiguration {
    public static void main(String[] args) {
        SpringApplication.run(JournalServiceConfiguration.class, args);
    }

    @Bean
    @ConditionalOnClass(ConfigurationCustomizer.class)
    ConfigurationCustomizer mybatisConfigurationCustomizer() {
        return configuration -> {
            configuration.getTypeHandlerRegistry().register(IterableTypeHandler.class);
            configuration.setLocalCacheScope(LocalCacheScope.STATEMENT);
            configuration.setJdbcTypeForNull(JdbcType.NULL);
        };
    }
}
