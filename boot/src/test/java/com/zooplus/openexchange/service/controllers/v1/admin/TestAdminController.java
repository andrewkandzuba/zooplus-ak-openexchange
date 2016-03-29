package com.zooplus.openexchange.service.controllers.v1.admin;

import com.zooplus.openexchange.protocol.v1.Status;
import com.zooplus.openexchange.service.controllers.v1.ControllerStarter;
import com.zooplus.openexchange.service.controllers.v1.TestApiMockDbController;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
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
public class TestAdminController extends TestApiMockDbController {
    @Test
    public void testAdminStatusPath() throws Throwable {
        // Make a request
        Status response =
                client
                        .exchange(
                                provideEndPoint() + "/" + STATUS_PATH,
                                HttpMethod.GET,
                                new HttpEntity<>(adminHeaders),
                                Status.class).getBody();

        Assert.assertNotNull(response);
        Assert.assertEquals(response.getInstanceId(), "7935a0789a204973ab70b6f01458b4f3");
    }
}
