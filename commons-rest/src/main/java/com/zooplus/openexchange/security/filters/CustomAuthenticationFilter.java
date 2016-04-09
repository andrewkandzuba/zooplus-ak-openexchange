package com.zooplus.openexchange.security.filters;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.csrf.CsrfTokenRepository;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.UrlPathHelper;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

public abstract class CustomAuthenticationFilter extends OncePerRequestFilter {
    private final static Logger logger = LoggerFactory.getLogger(CustomAuthenticationFilter.class);
    @Autowired
    protected AuthenticationManager authenticationManager;

    public abstract String[] permitAdminEndpoints();
    public abstract String[] permitAllEndpoints();

    public abstract String logoutEndpoint();
    public abstract String loginEndpoint();

    public abstract String[] permitCsrfEndpoints();
    public abstract CsrfTokenRepository csrfTokenRepository();

    protected abstract void processWithCustomAuthentication(HttpServletRequest httpRequest,
                                                            HttpServletResponse httpResponse) throws IOException;

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

    protected Authentication tryToAuthenticateWithUsernameAndPassword(Optional<String> username, Optional<String> password) {
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
        return loginEndpoint().equalsIgnoreCase(resourcePath)
                && httpRequest.getMethod().equals("POST");
    }
}