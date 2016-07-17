package com.zooplus.openexchange.tests.unit;

import com.zooplus.openexchange.clients.SockJsRxClient;
import com.zooplus.openexchange.controllers.MessageProcessor;
import com.zooplus.openexchange.controllers.v1.FakeMessage;
import com.zooplus.openexchange.protocol.ws.v1.CurrencyListRequest;
import com.zooplus.openexchange.protocol.ws.v1.CurrencyListResponse;
import com.zooplus.openexchange.protocol.ws.v1.HistoricalQuotesRequest;
import com.zooplus.openexchange.protocol.ws.v1.HistoricalQuotesResponse;
import com.zooplus.openexchange.starters.UnitTestStarter;
import com.zooplus.openexchange.utils.MessageConvetor;
import org.apache.commons.lang.time.DateFormatUtils;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.http.HttpHeaders;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.socket.WebSocketHttpHeaders;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import static com.zooplus.openexchange.controllers.v1.Version.*;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(UnitTestStarter.class)
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

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("x-auth-token", "some-test-value");
        sockJsClient.doHandshake("http://localhost:" + port + API_PATH_V1 + WS_ENDPOINT + CURRENCIES_WS_ENDPOINT,
                new WebSocketHttpHeaders(httpHeaders),
                errorMessage -> {
                },
                (MessageProcessor) (session, message, payloadClass) -> {
                    Assert.assertTrue(payloadClass.equals(CurrencyListResponse.class));
                    Assert.assertTrue(message instanceof CurrencyListResponse);
                    CurrencyListResponse response = (CurrencyListResponse) message;
                    Assert.assertEquals(response.getCurrencies().getCurrencies().size(), 1);
                    Assert.assertTrue(response.getCurrencies().getCurrencies().entrySet()
                            .stream().anyMatch(currency -> currency.getKey().equals("USD")));
                    isReplyReceived.compareAndSet(false, true);
                    session.close();
                    reply.countDown();
                    return true;
                })
                .addCallback(
                        session -> {
                            connected.countDown();
                            try {
                                CurrencyListRequest request = new CurrencyListRequest();
                                request.setTop(10);
                                session.sendMessage(MessageConvetor.to(request, CurrencyListRequest.class));
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
    public void testHistoricalQuotes() throws Exception {
        CountDownLatch connected = new CountDownLatch(1);
        CountDownLatch reply = new CountDownLatch(1);
        AtomicBoolean isReplyReceived = new AtomicBoolean(false);

        sockJsClient.doHandshake("http://localhost:" + port + API_PATH_V1 + WS_ENDPOINT + CURRENCIES_WS_ENDPOINT,
                errorMessage -> {
                },
                (session, message, payloadClass) -> {
                    Assert.assertTrue(payloadClass.equals(HistoricalQuotesResponse.class));
                    Assert.assertTrue(message instanceof HistoricalQuotesResponse);
                    HistoricalQuotesResponse response = (HistoricalQuotesResponse) message;
                    Assert.assertEquals(response.getQuotes().getQuotes().size(), 2);
                    Assert.assertTrue(response.getQuotes().getQuotes().entrySet()
                            .stream().anyMatch(rate -> rate.getKey().equals("EUR")));
                    Assert.assertTrue(response.getQuotes().getQuotes().entrySet()
                            .stream().anyMatch(rate -> rate.getKey().equals("UAH")));
                    isReplyReceived.compareAndSet(false, true);
                    session.close();
                    reply.countDown();
                    return true;
                })
                .addCallback(
                        session -> {
                            connected.countDown();
                            try {
                                HistoricalQuotesRequest request = new HistoricalQuotesRequest();
                                request.setCurrencyCode("USD");
                                request.setExchangeDate(DateFormatUtils.format(System.currentTimeMillis(), "yyyy-MM-dd"));
                                session.sendMessage(MessageConvetor.to(request, HistoricalQuotesRequest.class));
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
                errorMessage -> {
                    isErrorReceived.compareAndSet(false, true);
                    reply.countDown();
                })
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
