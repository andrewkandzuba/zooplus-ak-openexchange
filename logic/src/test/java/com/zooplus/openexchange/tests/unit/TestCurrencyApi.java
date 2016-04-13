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
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.client.WebSocketClient;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import org.springframework.web.socket.sockjs.client.RestTemplateXhrTransport;
import org.springframework.web.socket.sockjs.client.SockJsClient;
import org.springframework.web.socket.sockjs.client.Transport;
import org.springframework.web.socket.sockjs.client.WebSocketTransport;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

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
        AtomicBoolean isReplyReceived = new AtomicBoolean(false);

        // Connect to server WS
        sockJsClient.doHandshake(new TextWebSocketHandler() {
            @Override
            protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
                Object t = MessageConvetor.from(message.getPayload());
                Assert.assertTrue(t instanceof CurrenciesListResponse);
                CurrenciesListResponse response = (CurrenciesListResponse) t;
                Assert.assertEquals(response.getCurrencies().size(), 1);
                Assert.assertTrue(response.getCurrencies().stream().anyMatch(currency -> currency.getCode().equals("USD")));
                isReplyReceived.compareAndSet(false, true);
                session.close();
                reply.countDown();
            }

            @Override
            public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
                super.handleTransportError(session, exception);
            }
        }, "http://localhost:" + port + API_PATH_V1 + WS_ENDPOINT + CURRENCIES_WS_ENDPOINT)
                .addCallback(
                        session -> {
                            connected.countDown();
                            try {
                                CurrenciesListRequest request = new CurrenciesListRequest();
                                request.setTop(10);
                                session.sendMessage(new TextMessage(MessageConvetor.to(request, CurrenciesListRequest.class)));
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        },
                        Throwable::printStackTrace);
        Assert.assertTrue(connected.await(3000, TimeUnit.MILLISECONDS));
        Assert.assertTrue(reply.await(3000, TimeUnit.MILLISECONDS));
        Assert.assertTrue(isReplyReceived.get());
    }

    @AfterClass
    public static void tearDown() throws Exception {
    }
}
