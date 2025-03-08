package ru.alexander.projects.auths.infrastructure.configurations;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Primary;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.FormLoginConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import ru.alexander.projects.auths.infrastructure.filters.AuthenticationFilter;

import javax.sql.DataSource;

@Configuration
@Import(value = BCryptPasswordEncoder.class)
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class WebSecurityConfiguration {
    @Value(value = "${auths.endpoints.allowed}")
    private final String[] allowedEndpoints;

    @Bean
    @SneakyThrows
    public SecurityFilterChain securityFilterChain(
            HttpSecurity httpSecurity,
            AuthenticationFilter authenticationFilter,
            AuthenticationEntryPoint authenticationEntryPoint,
            AccessDeniedHandler accessDeniedHandler
    ) {
        return httpSecurity
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers(allowedEndpoints).permitAll()
                        .anyRequest().authenticated()
                )
                .exceptionHandling(handling -> handling
                        .authenticationEntryPoint(authenticationEntryPoint)
                        .accessDeniedHandler(accessDeniedHandler)
                )
                .addFilterBefore(authenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .formLogin(FormLoginConfigurer::disable)
                .build();
    }

    @Bean
    @Primary
    public UserDetailsManager userDetailsManager(
            DataSource dataSource
    ) {
        return new JdbcUserDetailsManager(dataSource);
    }
}
