package com.zooplus.openexchange.mockers;

import com.zooplus.openexchange.protocol.v1.Loginresponse;
import com.zooplus.openexchange.service.security.SecurityConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.ResponseErrorHandler;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;

import java.io.IOException;

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

    protected final HttpHeaders adminHeaders;
    protected final RestTemplate client;

    public TestApiController() {
        this.adminHeaders = new HttpHeaders();
        this.client = new RestTemplate();
        this.client.setErrorHandler(new ResponseErrorHandler() {
            @Override
            public boolean hasError(ClientHttpResponse clientHttpResponse) throws IOException {
                return clientHttpResponse.getStatusCode() != HttpStatus.OK;
            }

            @Override
            public void handleError(ClientHttpResponse clientHttpResponse) throws IOException {
                // ToDo: do something useful here
            }
        });
    }

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
