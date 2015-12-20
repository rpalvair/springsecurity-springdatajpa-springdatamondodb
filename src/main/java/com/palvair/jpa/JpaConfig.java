package com.palvair.jpa;

import com.palvair.security.model.User;
import org.springframework.boot.orm.jpa.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.persistence.EntityListeners;

/**
 * Created by widdy on 20/12/2015.
 */
@Configuration
@EnableJpaAuditing
@EntityScan(basePackages = {"com.palvair.security.model"})
@EnableJpaRepositories(basePackages = {"com.palvair.jpa"})
@EnableTransactionManagement
public class JpaConfig {

    @Bean
    public AuditorAware<User> auditorProvider() {
        return new SpringSecurityAuditorAware();
    }

}
