package com.zooplus.openexchange.security.filters;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zooplus.openexchange.protocol.rest.v1.Loginresponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.UrlPathHelper;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

import static com.zooplus.openexchange.controllers.v1.Version.USER_LOGIN_PATH;
import static com.zooplus.openexchange.controllers.v1.Version.USERS_ENDPOINT;

@Component
public class DataSourceAuthenticationFilter extends OncePerRequestFilter {
    public final static String X_AUTH_USERNAME_HEADER = "x-auth-username";
    public final static String X_AUTH_PASSWORD_HEADER = "x-auth-password";
    public final static String X_AUTH_TOKEN_HEADER = "x-auth-token";
    private final static Logger logger = LoggerFactory.getLogger(DataSourceAuthenticationFilter.class);
    @Autowired
    private AuthenticationManager authenticationManager;

    @Override
    protected void doFilterInternal(HttpServletRequest httpRequest, HttpServletResponse httpResponse, FilterChain chain) throws ServletException, IOException {
        try {
            if (postToAuthenticate(httpRequest)) {
                processWithCustomAuthentication(httpRequest, httpResponse);
                return;
            }
            logger.debug("CustomAuthenticationFilter is passing request down the filter chain");
            chain.doFilter(httpRequest, httpResponse);
        } catch (InternalAuthenticationServiceException internalAuthenticationServiceException) {
            SecurityContextHolder.clearContext();
            logger.error("Internal authentication service exception", internalAuthenticationServiceException);
            httpResponse.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        } catch (AuthenticationException authenticationException) {
            SecurityContextHolder.clearContext();
            httpResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED, authenticationException.getMessage());
        }
    }

    private void processWithCustomAuthentication(HttpServletRequest httpRequest,
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

    private Authentication tryToAuthenticateWithUsernameAndPassword(Optional<String> username, Optional<String> password) {
        UsernamePasswordAuthenticationToken requestAuthentication = new UsernamePasswordAuthenticationToken(username, password);
        return tryToAuthenticate(requestAuthentication);
    }

    private Authentication tryToAuthenticate(Authentication requestAuthentication) {
        Authentication responseAuthentication = authenticationManager.authenticate(requestAuthentication);
        if (responseAuthentication == null || !responseAuthentication.isAuthenticated()) {
            throw new InternalAuthenticationServiceException("Unable to authenticate Domain User for provided credentials");
        }
        logger.debug("User successfully authenticated");
        return responseAuthentication;
    }

    private boolean postToAuthenticate(HttpServletRequest httpRequest) {
        String resourcePath = new UrlPathHelper().getPathWithinApplication(httpRequest);
        return resourcePath.equalsIgnoreCase(USERS_ENDPOINT + USER_LOGIN_PATH) && HttpMethod.POST.matches(httpRequest.getMethod());
    }
}
