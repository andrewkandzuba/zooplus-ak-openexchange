package com.zooplus.openexchange.clients.test;

import com.zooplus.openexchange.protocol.v1.Loginresponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.ResponseErrorHandler;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import static com.zooplus.openexchange.service.controllers.v1.ApiController.USER_AUTHENTICATE_PATH;
import static com.zooplus.openexchange.service.security.SecurityConfig.*;

public abstract class TestAbstractClient {
    private final static Logger logger = LoggerFactory.getLogger(TestAbstractClient.class);
    private static final String TEST_ENDPOINT_TEMPLATE = "http://localhost:%s";
    @Value("${local.server.port}")
    private int port;
    private final RestTemplate client;

    public TestAbstractClient() {
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

    public RestTemplate getClient() {
        return client;
    }

    protected String provideEndPoint(){
        return String.format(TEST_ENDPOINT_TEMPLATE, port);
    }

    protected abstract void loginAsAdmin();

    protected String loginWithUserNameAndPassword(String userName, String password){
        HttpHeaders headers = new HttpHeaders();
        headers.add(X_AUTH_USERNAME_HEADER, userName);
        headers.add(X_AUTH_PASSWORD_HEADER, password);

        // Send login request
        ResponseEntity<Loginresponse> loginResp = getClient()
                .exchange(
                        provideEndPoint() + "/" + USER_AUTHENTICATE_PATH,
                        HttpMethod.POST,
                        new HttpEntity<>(headers),
                        Loginresponse.class);
        logger.info("----------------------------------------------------------------------");
        for(Map.Entry<String, List<String>> header : loginResp.getHeaders().entrySet()){
            StringBuilder sb = new StringBuilder();
            sb.append(String.format("[Header : %s ( ", header.getKey()));
            for(String value : header.getValue()) {
                sb.append(String.format(" %s ", value));
            }
            sb.append(" )]");
            logger.info(sb.toString());
        }
        logger.info("----------------------------------------------------------------------");
        // Remember admin authentication token
        return loginResp.getHeaders().toSingleValueMap().getOrDefault(X_AUTH_TOKEN_HEADER, "");
    }

    protected String get(String token, String path, HttpMethod method, String... params){
        HttpHeaders headers = new HttpHeaders();
        headers.add(X_AUTH_TOKEN_HEADER, token);

        // Send login request
        ResponseEntity<String> resp = getClient()
                .exchange(
                        provideEndPoint() + "/" + path,
                        method,
                        new HttpEntity<>(headers),
                        String.class);
        logger.info("----------------------------------------------------------------------");
        for(Map.Entry<String, List<String>> header : resp.getHeaders().entrySet()){
            StringBuilder sb = new StringBuilder();
            sb.append(String.format("[Header : %s ( ", header.getKey()));
            for(String value : header.getValue()) {
                sb.append(String.format(" %s ", value));
            }
            sb.append(" )]");
            logger.info(sb.toString());
        }
        logger.info("----------------------------------------------------------------------");
        // Remember admin authentication token
        return resp.getBody();
    }

    @PostConstruct
    private void initAdminHeaders() {
        loginAsAdmin();
    }
}
