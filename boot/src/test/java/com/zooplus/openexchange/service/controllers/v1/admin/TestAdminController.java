package com.zooplus.openexchange.service.controllers.v1.admin;

import com.zooplus.openexchange.protocol.v1.Status;
import com.zooplus.openexchange.service.controllers.v1.ControllerStarter;
import com.zooplus.openexchange.service.controllers.v1.TestApiController;
import com.zooplus.openexchange.service.data.repositories.UserRepository;
import com.zooplus.openexchange.service.utils.SequenceGenerator;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static com.zooplus.openexchange.service.controllers.v1.ApiController.STATUS_PATH;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(ControllerStarter.class)
@WebIntegrationTest("server.port:0")
@ActiveProfiles("test")
public class TestAdminController extends TestApiController {
    private static final String TEST_ENDPOINT_TEMPLATE = "http://localhost:%s%s";
    @Value("${local.server.port}")
    private int port;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private SequenceGenerator generator;

    @Before
    public void setUp() throws Exception {
        mockAdminAccess(userRepository,generator);
    }

    @Test
    public void testAdminStatusPath() throws Throwable {
        // Make a request
        Status response = restTemplate.exchange(
                String.format(TEST_ENDPOINT_TEMPLATE, port, STATUS_PATH),
                HttpMethod.GET,
                new HttpEntity<>(headers),
                Status.class).getBody();

        Assert.assertNotNull(response);
        Assert.assertEquals(response.getInstanceId(), "7935a0789a204973ab70b6f01458b4f3");
    }
}
