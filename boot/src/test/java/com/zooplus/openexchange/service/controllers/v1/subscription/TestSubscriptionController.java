package com.zooplus.openexchange.service.controllers.v1.subscription;

import com.zooplus.openexchange.service.data.domain.Subscriber;
import com.zooplus.openexchange.service.data.repositories.SubscriberRepository;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.WebApplicationContext;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(SubscriptionControllerStater.class)
@WebIntegrationTest("server.port:0")
@ActiveProfiles("development")
public class TestSubscriptionController {
    @Value("${local.server.port}")
    int port;

    private MockMvc mockMvc;

    @Autowired
    private SubscriberRepository subscriberRepository;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Before
    public void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    public void testSubscription() throws Exception {
        Subscriber s = new Subscriber();
        s.setId(1L);
        s.setEmail("RS@AK.COM");
        s.setPassword("1234");
        MockitoAnnotations.initMocks(this);
        Mockito.when(subscriberRepository.findByEmail("RS@AK.COM")).thenReturn(null);
        Mockito.when(subscriberRepository.saveAndFlush(Mockito.any(Subscriber.class))).thenReturn(s);

        RestTemplate restTemplate = new RestTemplate();
        Subscriber subscriber = new Subscriber();
        subscriber.setEmail("RS@AK.COM");
        subscriber.setPassword("test1");
        Subscriber response = restTemplate.postForObject(
                String.format("http://localhost:%s/%s", port, SubscriptionController.SUBSCRIBES_PATH), subscriber, Subscriber.class);

        Assert.assertNotNull(response);
        Assert.assertEquals(response.getId().longValue(), 1L);

    }
}
