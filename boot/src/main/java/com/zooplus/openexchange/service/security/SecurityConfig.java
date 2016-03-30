package com.zooplus.openexchange.service.security;

import com.zooplus.openexchange.service.controllers.v1.ApiController;
import com.zooplus.openexchange.service.security.filters.AuthenticationFilter;
import com.zooplus.openexchange.service.security.providers.DataSourceAuthenticationProvider;
import com.zooplus.openexchange.service.security.providers.TokenAuthenticationProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.http.HttpServletResponse;

@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    public final static String X_AUTH_USERNAME_HEADER = "X-Auth-Username";
    public final static String X_AUTH_PASSWORD_HEADER = "X-Auth-Password";
    public final static String X_AUTH_TOKEN_HEADER = "X-Auth-Token";

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                .antMatchers(actuatorEndpoints()).hasRole("ADMIN")
                .antMatchers(allPermittedEndpoints()).permitAll()
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
        auth.authenticationProvider(backendAdminUsernamePasswordAuthenticationProvider()).
                authenticationProvider(tokenAuthenticationProvider());
    }

    @Bean
    public  AuthenticationProvider backendAdminUsernamePasswordAuthenticationProvider() {
        return new DataSourceAuthenticationProvider();
    }

    @Bean
    public  AuthenticationProvider tokenAuthenticationProvider() {
        return new TokenAuthenticationProvider();
    }

    @Bean
    public  AuthenticationEntryPoint unauthorizedEntryPoint() {
        return (request, response, authException) -> response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
    }

    private String[] actuatorEndpoints() {
        return new String[]{ApiController.ADMIN_ENDPOINT, ApiController.USER_REGISTRATION_PATH};
    }

    private String[] allPermittedEndpoints() {
        return new String[]{ApiController.USER_AUTHENTICATE_PATH};
    }
}
