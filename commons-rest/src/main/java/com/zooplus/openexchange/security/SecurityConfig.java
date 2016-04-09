package com.zooplus.openexchange.security;

import com.zooplus.openexchange.security.filters.CsrfTokenReflectionFilter;
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

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http.csrf().ignoringAntMatchers(customAuthenticationFilter.permitCsrfEndpoints())
                .and()
                .authenticationProvider(customAuthenticationProvider)
                .authorizeRequests()
                .antMatchers(customAuthenticationFilter.permitAdminEndpoints()).hasRole("ADMIN")
                .antMatchers(customAuthenticationFilter.permitAllEndpoints()).permitAll()
                .and()
                .addFilterAfter(
                        new CsrfTokenReflectionFilter(),
                        CsrfFilter.class)
                .addFilterBefore(
                        customAuthenticationFilter,
                        BasicAuthenticationFilter.class)
                .exceptionHandling().authenticationEntryPoint(unauthorizedEntryPoint());
                /*.and().logout()
                .logoutRequestMatcher(new AntPathRequestMatcher(customAuthenticationFilter.logoutEndpoint()));*/
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
