package com.zooplus.openexchange.security;

import com.zooplus.openexchange.security.filters.CsrfTokenGeneratorFilter;
import com.zooplus.openexchange.security.filters.CustomAuthenticationFilter;
import com.zooplus.openexchange.security.providers.CustomAuthenticationProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.csrf.CsrfFilter;
import org.springframework.security.web.csrf.CsrfTokenRepository;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;
import org.springframework.session.web.http.HeaderHttpSessionStrategy;

import javax.servlet.http.HttpServletResponse;

@EnableWebSecurity
@EnableRedisHttpSession
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    private CustomAuthenticationFilter customAuthenticationFilter;
    @Autowired
    private CustomAuthenticationProvider customAuthenticationProvider;
    @Autowired
    private CsrfTokenRepository csrfTokenRepository;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf()
                .ignoringAntMatchers(customAuthenticationFilter.permitCsrfEndpoints())
                .csrfTokenRepository(csrfTokenRepository)
                .and()
                .addFilterBefore(
                        new CsrfTokenGeneratorFilter(csrfTokenRepository),
                        CsrfFilter.class);

        http
                .authenticationProvider(customAuthenticationProvider)
                .authorizeRequests().anyRequest().authenticated()
                .antMatchers(customAuthenticationFilter.actuatorEndpoints()).hasRole("ADMIN")
                .antMatchers(customAuthenticationFilter.permitSecurityEndpoints()).permitAll()
                .and()
                .anonymous().disable()
                .exceptionHandling().authenticationEntryPoint(unauthorizedEntryPoint());

        http
                .addFilterBefore(
                        customAuthenticationFilter,
                        BasicAuthenticationFilter.class
                );

    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(customAuthenticationProvider);
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
}
