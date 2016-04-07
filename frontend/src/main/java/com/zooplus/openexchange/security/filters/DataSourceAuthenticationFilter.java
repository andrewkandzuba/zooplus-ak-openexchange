package com.zooplus.openexchange.security.filters;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zooplus.openexchange.protocol.v1.Loginresponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.csrf.CsrfTokenRepository;
import org.springframework.security.web.csrf.HttpSessionCsrfTokenRepository;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

import static com.zooplus.openexchange.controllers.v1.Version.*;

@Component
public class DataSourceAuthenticationFilter extends CustomAuthenticationFilter {
    public final static String X_AUTH_USERNAME_HEADER = "x-auth-username";
    public final static String X_AUTH_PASSWORD_HEADER = "x-auth-password";
    public final static String X_AUTH_TOKEN_HEADER = "x-auth-token";

    private final static Logger logger = LoggerFactory.getLogger(DataSourceAuthenticationFilter.class);
    protected void processWithCustomAuthentication(HttpServletRequest httpRequest,
                                                   HttpServletResponse httpResponse) throws IOException {

        Optional<String> username = Optional.ofNullable(httpRequest.getHeader(X_AUTH_USERNAME_HEADER));
        Optional<String> password = Optional.ofNullable(httpRequest.getHeader(X_AUTH_PASSWORD_HEADER));
        logger.debug("Trying to authenticate user {} by X-Auth-Username method", username);
        Authentication resultOfAuthentication = tryToAuthenticateWithUsernameAndPassword(username, password);
        SecurityContextHolder.getContext().setAuthentication(resultOfAuthentication);
        httpResponse.setStatus(HttpServletResponse.SC_OK);
        Loginresponse loginresponse = new Loginresponse();
        String tokenJsonResponse = new ObjectMapper().writeValueAsString(loginresponse);
        httpResponse.addHeader("Content-Type", "application/json");
        httpResponse.getWriter().print(tokenJsonResponse);
    }

    @Override
    public String[] actuatorEndpoints() {
        return new String[]{ADMIN_ENDPOINT, USER_REGISTRATION_PATH};
    }

    @Override
    public String[] permitSecurityEndpoints() {
        return new String[]{USER_LOGIN_PATH};
    }

    @Override
    public String[] permitCsrfEndpoints() {
        return new String[]{USER_LOGIN_PATH, USER_LOGOUT_PATH};
    }

    @Override
    public String authenticationPath() {
        return USER_LOGIN_PATH;
    }

    @Override
    @Bean
    public CsrfTokenRepository csrfTokenRepository() {
        return new HttpSessionCsrfTokenRepository();
    }
}
