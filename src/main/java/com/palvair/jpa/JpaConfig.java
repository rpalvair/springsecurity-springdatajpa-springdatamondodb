package com.palvair.jpa;

import com.palvair.security.model.User;
import org.springframework.boot.orm.jpa.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * Created by widdy on 20/12/2015.
 */
@Configuration
@EntityScan(basePackages = {"com.palvair.security.model"})
@EnableJpaRepositories(basePackages = {"com.palvair.jpa"})
@EnableJpaAuditing
public class JpaConfig {

    @Bean
    public AuditorAware<User> auditorProvider() {
        return new SpringSecurityAuditorAware();
    }
}
