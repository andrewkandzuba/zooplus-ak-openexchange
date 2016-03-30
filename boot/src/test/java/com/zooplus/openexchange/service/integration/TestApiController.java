package com.zooplus.openexchange.service.integration;

import com.zooplus.openexchange.protocol.v1.Loginresponse;
import com.zooplus.openexchange.service.security.SecurityConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;

import static com.zooplus.openexchange.service.controllers.v1.ApiController.USER_AUTHENTICATE_PATH;
import static com.zooplus.openexchange.service.security.SecurityConfig.*;

public abstract class TestApiController {
    private static final String TEST_ENDPOINT_TEMPLATE = "http://localhost:%s";

    @Value("${local.server.port}")
    private int port;
    @Value("${admin.name}")
    protected String adminName;
    @Value("${admin.password}")
    protected String adminPassword;
    @Value("${admin.email}")
    protected String adminEmail;

    protected HttpHeaders adminHeaders = new HttpHeaders();
    protected RestTemplate client = new RestTemplate();

    @PostConstruct
    private void initAdminHeaders() {
        preInits();
        // Add adminHeaders
        HttpHeaders headers = new HttpHeaders();
        headers.add(X_AUTH_USERNAME_HEADER, adminName);
        headers.add(X_AUTH_PASSWORD_HEADER, adminPassword);

        // Send login request
        ResponseEntity<Loginresponse> loginResp = new RestTemplate().exchange(
                provideEndPoint() + "/" + USER_AUTHENTICATE_PATH,
                HttpMethod.POST,
                new HttpEntity<>(headers),
                Loginresponse.class);

        // Add admin authentication token to admin headers
        adminHeaders.add(SecurityConfig.X_AUTH_TOKEN_HEADER, loginResp.getHeaders().toSingleValueMap().getOrDefault(X_AUTH_TOKEN_HEADER, ""));
    }

    protected void preInits(){}

    protected String provideEndPoint(){
        return String.format(TEST_ENDPOINT_TEMPLATE, port);
    }
}
