package com.zooplus.openexchange.security.configurations.stubs;


import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.security.web.csrf.CsrfTokenRepository;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

public class CsrfTokenRepositoryStub implements CsrfTokenRepository {
    private final static String stubCsrfTonenValue = UUID.randomUUID().toString();
    private final static CsrfToken token = new CsrfToken() {
        @Override
        public String getHeaderName() {
            return "X-CSRF-TOKEN";
        }

        @Override
        public String getParameterName() {
            return "X-CSRF-PARAM";
        }

        @Override
        public String getToken() {
            return stubCsrfTonenValue;
        }
    };

    @Override
    public CsrfToken generateToken(HttpServletRequest httpServletRequest) {
        return token;
    }

    @Override
    public void saveToken(CsrfToken csrfToken, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
        // NOP
    }

    @Override
    public CsrfToken loadToken(HttpServletRequest httpServletRequest) {
        return token;
    }
}
