package com.zooplus.openexchange.tests.unit;

import com.zooplus.openexchange.clients.SockJsRxClient;
import com.zooplus.openexchange.controllers.MessageProcessor;
import com.zooplus.openexchange.protocol.v1.FakeMessage;
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
import org.springframework.web.socket.WebSocketSession;

import java.util.Optional;
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
    private static SockJsRxClient sockJsClient;

    @BeforeClass
    public static void setUp() throws Exception {
        sockJsClient = SockJsRxClient.builder().get();
    }

    @Test
    public void testCurrencyList() throws Exception {
        CountDownLatch connected = new CountDownLatch(1);
        CountDownLatch reply = new CountDownLatch(1);
        AtomicBoolean isReplyReceived = new AtomicBoolean(false);

        sockJsClient.doHandshake("http://localhost:" + port + API_PATH_V1 + WS_ENDPOINT + CURRENCIES_WS_ENDPOINT,
                Optional.empty(),
                new MessageProcessor() {
                    @Override
                    public boolean supports(Class<?> payloadClass) {
                        return payloadClass.equals(CurrenciesListResponse.class);
                    }

                    @Override
                    public void handle(WebSocketSession session, Object message) throws Exception {
                        Assert.assertTrue(message instanceof CurrenciesListResponse);
                        CurrenciesListResponse response = (CurrenciesListResponse) message;
                        Assert.assertEquals(response.getCurrencies().size(), 1);
                        Assert.assertTrue(response.getCurrencies().stream().anyMatch(currency -> currency.getCode().equals("USD")));
                        isReplyReceived.compareAndSet(false, true);
                        session.close();
                        reply.countDown();
                    }
                })
                .addCallback(
                        session -> {
                            connected.countDown();
                            try {
                                CurrenciesListRequest request = new CurrenciesListRequest();
                                request.setTop(10);
                                session.sendMessage(MessageConvetor.to(request, CurrenciesListRequest.class));
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        },
                        Throwable::printStackTrace);

        Assert.assertTrue(connected.await(3000, TimeUnit.MILLISECONDS));
        Assert.assertTrue(reply.await(3000, TimeUnit.MILLISECONDS));
        Assert.assertTrue(isReplyReceived.get());
    }

    @Test
    public void testMessageProcessorNotFound() throws Exception {
        CountDownLatch connected = new CountDownLatch(1);
        CountDownLatch reply = new CountDownLatch(1);
        AtomicBoolean isErrorReceived = new AtomicBoolean(false);

        sockJsClient.doHandshake(
                "http://localhost:" + port + API_PATH_V1 + WS_ENDPOINT + CURRENCIES_WS_ENDPOINT,
                Optional.of(errorMessage -> {
                    isErrorReceived.compareAndSet(false, true);
                    reply.countDown();
                }))
                .addCallback(
                        session -> {
                            connected.countDown();
                            try {
                                session.sendMessage(MessageConvetor.to(new FakeMessage(), FakeMessage.class));
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        },
                        Throwable::printStackTrace);
        Assert.assertTrue(connected.await(3000, TimeUnit.MILLISECONDS));
        Assert.assertTrue(reply.await(3000, TimeUnit.MILLISECONDS));
        Assert.assertTrue(isErrorReceived.get());
    }

    @AfterClass
    public static void tearDown() throws Exception {
    }
}
