package ru.alexander.projects.auths.infrastructure.configurations;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;

@Configuration
@Import(value = JdbcUserDetailsManager.class)
public class WebSecurityConfiguration {

    @Bean
    public UserDetailsManager jdbcUserDetailsManager(JdbcUserDetailsManager manager) {
        return manager;
    }
}
