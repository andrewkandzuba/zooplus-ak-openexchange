package com.zooplus.openexchange.clients.integration;

import com.zooplus.openexchange.clients.test.TestAbstractClient;
import org.springframework.beans.factory.annotation.Value;

public class TestIntegrationClient extends TestAbstractClient {
    @Value("${admin.name}")
    private String adminName;
    @Value("${admin.password}")
    private String adminPassword;
    protected String adminSessionToken;

    @Override
    protected void loginAsAdmin() {
        // Remember admin authentication token
        adminSessionToken = loginWithUserNameAndPassword(adminName, adminPassword);
    }
}
