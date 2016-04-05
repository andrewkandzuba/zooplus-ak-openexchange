package com.zooplus.openexchange.service.security;

import com.zooplus.openexchange.service.controllers.v1.ApiController;
import com.zooplus.openexchange.service.security.filters.AuthenticationFilter;
import com.zooplus.openexchange.service.security.providers.DataSourceAuthenticationProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;
import org.springframework.session.web.http.HeaderHttpSessionStrategy;

import javax.servlet.http.HttpServletResponse;

@EnableRedisHttpSession
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    public final static String X_AUTH_USERNAME_HEADER = "x-auth-username";
    public final static String X_AUTH_PASSWORD_HEADER = "x-auth-password";
    public final static String X_AUTH_TOKEN_HEADER = "x-auth-token";

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .authorizeRequests()
                .antMatchers(actuatorEndpoints()).hasRole("ADMIN")
                .antMatchers(permitAllEndpoints()).permitAll()
                .anyRequest().authenticated()
                .and()
                .anonymous().disable()
                .exceptionHandling().authenticationEntryPoint(unauthorizedEntryPoint());

        http
                .addFilterBefore(
                        new AuthenticationFilter(authenticationManager()),
                        BasicAuthenticationFilter.class
                );
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(dataSourceUsernamePasswordAuthenticationProvider());
    }

    @Bean
    public AuthenticationProvider dataSourceUsernamePasswordAuthenticationProvider() {
        return new DataSourceAuthenticationProvider();
    }

    @Bean
    public AuthenticationEntryPoint unauthorizedEntryPoint() {
        return (request, response, authException) -> response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
    }

    private String[] actuatorEndpoints() {
        return new String[]{ApiController.ADMIN_ENDPOINT, ApiController.USER_REGISTRATION_PATH};
    }

    private String[] permitAllEndpoints() {
        return new String[]{ApiController.USER_AUTHENTICATE_PATH};
    }

    @Bean
    public HeaderHttpSessionStrategy sessionStrategy() {
        return new HeaderHttpSessionStrategy();
    }
}
