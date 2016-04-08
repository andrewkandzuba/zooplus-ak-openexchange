package com.zooplus.openexchange.tests.unit;

import com.zooplus.openexchange.starters.LogicStarter;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.integration.websocket.ClientWebSocketContainer;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;

import static com.zooplus.openexchange.controllers.v1.Version.API_PATH_V1;
import static com.zooplus.openexchange.controllers.v1.Version.CURRENCIES_LIST_API;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(LogicStarter.class)
@WebIntegrationTest("server.port:0")
@ActiveProfiles("logic")
public class TestCurrencyApi {
    @Value("${local.server.port}")
    private int port;
    private ClientWebSocketContainer clientWebSocketContainer;

    @Before
    public void setUp() throws Exception {
        clientWebSocketContainer =
                new ClientWebSocketContainer(new StandardWebSocketClient(),
                        String.format(
                                "ws://localhost:%s%s%s",
                                port,
                                API_PATH_V1,
                                CURRENCIES_LIST_API));
        clientWebSocketContainer.start();
        Assert.assertTrue(clientWebSocketContainer.isRunning());
    }

    @Test
    public void testCurrencyList() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
        clientWebSocketContainer.stop();
    }
}
