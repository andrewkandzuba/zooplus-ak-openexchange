package com.zooplus.openexchange.tests.unit;

import com.zooplus.openexchange.starters.ApiStarter;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketMessage;
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

import static com.zooplus.openexchange.controllers.v1.Version.API_PATH_V1;
import static com.zooplus.openexchange.controllers.v1.Version.WS_ENDPOINT;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(ApiStarter.class)
@WebIntegrationTest("server.port:0")
@ActiveProfiles("api")
public class TestCurrencyApi {
    private final static Logger logger = LoggerFactory.getLogger(TestCurrencyApi.class);
    @Value("${local.server.port}")
    private int port;
    private WebSocketClient sockJsClient;

    @Before
    public void setUp() throws Exception {
        List<Transport> transports = new ArrayList<>(2);
        transports.add(new WebSocketTransport(new StandardWebSocketClient()));
        transports.add(new RestTemplateXhrTransport());
        sockJsClient = new SockJsClient(transports);
    }

    @Test
    public void testCurrencyList() throws Exception {
        CountDownLatch connected = new CountDownLatch(1);
        CountDownLatch reply = new CountDownLatch(1);
        ListenableFuture<WebSocketSession> future = sockJsClient.doHandshake(new TextWebSocketHandler() {
            @Override
            public void afterConnectionEstablished(WebSocketSession session) throws Exception {
                connected.countDown();
            }

            @Override
            protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
                reply.countDown();
            }
        }, "http://localhost:" + port + API_PATH_V1 + WS_ENDPOINT);

        connected.await(1000L, TimeUnit.MILLISECONDS);

        WebSocketSession webSocketSession = future.get();
        Assert.assertNotNull(webSocketSession);
        Assert.assertTrue(webSocketSession.isOpen());

        WebSocketMessage<String> message = new TextMessage("text");
        webSocketSession.sendMessage(message);

        reply.await(1000, TimeUnit.MILLISECONDS);

        webSocketSession.close();
    }

    @After
    public void tearDown() throws Exception {
    }
}
