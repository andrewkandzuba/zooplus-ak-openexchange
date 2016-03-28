package com.zooplus.openexchange.service.controllers.v1.app;

import com.zooplus.openexchange.protocol.v1.Status;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.client.RestTemplate;

import java.util.Base64;

import static com.zooplus.openexchange.service.controllers.v1.Constants.*;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(AdminControllerStarter.class)
@WebIntegrationTest("server.port:0")
@ActiveProfiles("development")
public class TestAdminAppController {
    private static final String TEST_ENDPOINT_TEMPLATE = "http://localhost:%s%s";
    @Value("${local.server.port}")
    private int port;

    @Test
    public void testLifecycle() throws Throwable {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Basic " +
                Base64.getEncoder().encodeToString(("andrewka" + ":" + "bestadmin").getBytes()));
        HttpEntity<String> request = new HttpEntity<>(headers);

        RestTemplate restTemplate = new RestTemplate();
        Status response = restTemplate.exchange(
                String.format(TEST_ENDPOINT_TEMPLATE, port,  "/" + API + "/" + VERSION_1 + "/" + ADMIN + "/" + STATUS),
                HttpMethod.GET,
                request,
                Status.class).getBody();

        Assert.assertNotNull(response);
        Assert.assertEquals(response.getInstanceId(), "7935a0789a204973ab70b6f01458b4f3");
    }
}
