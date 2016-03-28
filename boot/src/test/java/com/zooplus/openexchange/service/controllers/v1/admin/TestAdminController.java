package com.zooplus.openexchange.service.controllers.v1.admin;

import com.zooplus.openexchange.protocol.v1.Status;
import com.zooplus.openexchange.service.controllers.v1.ControllerStarter;
import com.zooplus.openexchange.service.data.repositories.UserRepository;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
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

import static com.zooplus.openexchange.service.controllers.v1.ApiController.*;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(ControllerStarter.class)
@WebIntegrationTest("server.port:0")
@ActiveProfiles("test")
public class TestAdminController {
    private static final String TEST_ENDPOINT_TEMPLATE = "http://localhost:%s%s";
    @Value("${local.server.port}")
    private int port;
    @Autowired
    private UserRepository userRepository;

    @Test
    public void testLifecycle() throws Throwable {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Basic " +
                Base64.getEncoder().encodeToString(("andrewka" + ":" + "bestadmin").getBytes()));
        HttpEntity<String> request = new HttpEntity<>(headers);
        RestTemplate restTemplate = new RestTemplate();
        Status response = restTemplate.exchange(
                String.format(TEST_ENDPOINT_TEMPLATE, port, STATUS_PATH),
                HttpMethod.GET,
                request,
                Status.class).getBody();

        Assert.assertNotNull(response);
        Assert.assertEquals(response.getInstanceId(), "7935a0789a204973ab70b6f01458b4f3");
    }
}
