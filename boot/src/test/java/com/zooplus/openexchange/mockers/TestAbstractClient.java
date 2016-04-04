package com.zooplus.openexchange.mockers;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.ResponseErrorHandler;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import java.io.IOException;

public abstract class TestAbstractClient {
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

    @PostConstruct
    private void initAdminHeaders() {
        loginAsAdmin();
    }
}
