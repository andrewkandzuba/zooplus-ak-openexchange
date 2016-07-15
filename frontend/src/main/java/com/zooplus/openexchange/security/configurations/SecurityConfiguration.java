package com.zooplus.openexchange.security.configurations;

import com.zooplus.openexchange.security.filters.DataSourceAuthenticationFilter;
import com.zooplus.openexchange.security.providers.DataSourceAuthenticationProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.csrf.CsrfTokenRepository;
import org.springframework.security.web.csrf.HttpSessionCsrfTokenRepository;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;
import org.springframework.session.web.http.HeaderHttpSessionStrategy;

import javax.servlet.http.HttpServletResponse;

import static com.zooplus.openexchange.controllers.v1.Version.*;

@EnableWebSecurity
@EnableRedisHttpSession(maxInactiveIntervalInSeconds = 60)
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable();

        http
                .authenticationProvider(authenticationProvider())
                .authorizeRequests()
                    .antMatchers(API_PATH_V1 + USER_RESOURCE).hasRole("ADMIN")
                    .antMatchers(API_PATH_V1 + SESSION_RESOURCE).hasRole("USER")
                    .anyRequest().permitAll()
                .and()
                .addFilterBefore(
                        authenticationFilter(),
                        BasicAuthenticationFilter.class)
                .exceptionHandling()
                .authenticationEntryPoint(unauthorizedEntryPoint());
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(authenticationProvider());
    }

    @Bean
    public DataSourceAuthenticationProvider authenticationProvider() {
        return new DataSourceAuthenticationProvider();
    }

    @Bean
    public DataSourceAuthenticationFilter authenticationFilter() {
        return new DataSourceAuthenticationFilter();
    }

    @Bean
    public AuthenticationEntryPoint unauthorizedEntryPoint() {
        return (request, response, authException) -> response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
    }

    @Bean
    public HeaderHttpSessionStrategy sessionStrategy() {
        return new HeaderHttpSessionStrategy();
    }

    @Override
    @Bean
    public AuthenticationManager authenticationManager() throws Exception {
        return super.authenticationManager();
    }

    @Bean
    public CsrfTokenRepository csrfTokenRepository() {
        return new HttpSessionCsrfTokenRepository();
    }
}
