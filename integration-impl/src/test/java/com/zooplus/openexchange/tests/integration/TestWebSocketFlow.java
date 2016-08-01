package com.zooplus.openexchange.tests.integration;

import com.zooplus.openexchange.clients.SockJsRxClient;
import com.zooplus.openexchange.controllers.MessageProcessor;
import com.zooplus.openexchange.protocol.integration.CurrencyListRequest;
import com.zooplus.openexchange.protocol.integration.CurrencyListResponse;
import com.zooplus.openexchange.starters.IntegrationTestStarter;
import com.zooplus.openexchange.utils.MessageConvetor;
import org.junit.Assert;
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

import static com.zooplus.openexchange.protocol.integration.MetaInfo.CURRENCIES_RESOURCE;


@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(IntegrationTestStarter.class)
@WebIntegrationTest("server.port:0")
@ActiveProfiles("integration")
public class TestWebSocketFlow {
    @Value("${local.server.port}")
    private int port;

    @Test
    public void testCurrencyList() throws Exception {
        SockJsRxClient sockJsClient = SockJsRxClient.builder().get();

        CountDownLatch connected = new CountDownLatch(1);
        CountDownLatch reply = new CountDownLatch(1);
        AtomicBoolean isReplyReceived = new AtomicBoolean(false);

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("x-auth-token", "some-test-value");
        sockJsClient.doHandshake("http://localhost:" + port + CURRENCIES_RESOURCE,
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
}
