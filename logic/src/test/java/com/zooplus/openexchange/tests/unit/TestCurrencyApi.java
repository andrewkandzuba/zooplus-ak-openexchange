package com.zooplus.openexchange.tests.unit;

import com.zooplus.openexchange.protocol.ws.v1.CurrenciesListRequest;
import com.zooplus.openexchange.protocol.ws.v1.CurrenciesListResponse;
import com.zooplus.openexchange.starters.ApiStarter;
import com.zooplus.openexchange.utils.MessageConvetor;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.client.WebSocketClient;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import org.springframework.web.socket.sockjs.client.RestTemplateXhrTransport;
import org.springframework.web.socket.sockjs.client.SockJsClient;
import org.springframework.web.socket.sockjs.client.Transport;
import org.springframework.web.socket.sockjs.client.WebSocketTransport;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static com.zooplus.openexchange.controllers.v1.Version.*;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(ApiStarter.class)
@WebIntegrationTest("server.port:0")
@ActiveProfiles("api")
public class TestCurrencyApi {
    @Value("${local.server.port}")
    private int port;
    private static WebSocketClient sockJsClient;

    @BeforeClass
    public static void setUp() throws Exception {
        List<Transport> transports = new ArrayList<>(2);
        transports.add(new WebSocketTransport(new StandardWebSocketClient()));
        transports.add(new RestTemplateXhrTransport());
        sockJsClient = new SockJsClient(transports);
    }

    @Test
    public void testCurrencyList() throws Exception {
        CountDownLatch connected = new CountDownLatch(1);
        CountDownLatch reply = new CountDownLatch(1);

        // Connect to server WS
        ListenableFuture<WebSocketSession> future = sockJsClient.doHandshake(new TextWebSocketHandler() {
            @Override
            public void afterConnectionEstablished(WebSocketSession session) throws Exception {
                connected.countDown();
            }

            @Override
            protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
                Object t = MessageConvetor.from(message.getPayload());
                Assert.assertTrue(t instanceof CurrenciesListResponse);
                CurrenciesListResponse response = (CurrenciesListResponse) t;
                Assert.assertEquals(response.getCurrencies().size(), 1);
                Assert.assertTrue(response.getCurrencies().stream().anyMatch(currency -> currency.getCode().equals("USD")));
                reply.countDown();
            }
        }, "http://localhost:" + port + API_PATH_V1 + WS_ENDPOINT + CURRENCIES_WS_ENDPOINT);
        Assert.assertTrue(connected.await(3000, TimeUnit.MILLISECONDS));

        // Test session is opened and operating
        WebSocketSession webSocketSession = future.get();
        Assert.assertNotNull(webSocketSession);
        Assert.assertTrue(webSocketSession.isOpen());

        // Create message and send
        CurrenciesListRequest request = new CurrenciesListRequest();
        request.setTop(10);
        webSocketSession.sendMessage(new TextMessage(MessageConvetor.to(request, CurrenciesListRequest.class)));
        Assert.assertTrue(reply.await(3000, TimeUnit.MILLISECONDS));

        // close session
        webSocketSession.close();
    }

    @AfterClass
    public static void tearDown() throws Exception {
    }
}
