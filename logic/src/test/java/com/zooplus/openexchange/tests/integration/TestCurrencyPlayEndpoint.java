package com.zooplus.openexchange.tests.integration;

import com.zooplus.openexchange.protocol.integration.v1.Currencies;
import com.zooplus.openexchange.starters.IntegrationStarter;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.client.RestTemplate;

import java.net.InetSocketAddress;
import java.net.Proxy;
import java.util.Date;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import static com.zooplus.openexchange.integrations.stubs.CurrencyPlayerIntegrationFlow.INBOUND_CHANNEL_API_CURRENCYPLAYER_LIST;
import static com.zooplus.openexchange.integrations.stubs.CurrencyPlayerIntegrationFlow.OUTBOUND_CHANNEL_API_CURRENCYPLAYER_LIST;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(IntegrationStarter.class)
@WebIntegrationTest("server.port:0")
@ActiveProfiles("integration")
public class TestCurrencyPlayEndpoint {
    public static final String BASE_URL = "http://apilayer.net/api/";
    public static final String ENDPOINT_LIST = "live";

    @Value("${currencyplayer.api.accesskey}")
    public String ACCESS_KEY;
    @Value("${local.proxy.host}")
    public String proxyHost;
    @Value("${local.proxy.port}")
    public int proxyPort;

    @Test
    public void testCurrencies() throws Exception {
        SimpleClientHttpRequestFactory clientHttpRequestFactory = new SimpleClientHttpRequestFactory();
        Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(proxyHost, proxyPort));
        clientHttpRequestFactory.setProxy(proxy);

        RestTemplate restTemplate = new RestTemplate(clientHttpRequestFactory);
        ResponseEntity<Currencies> response =  restTemplate.exchange(
                BASE_URL + ENDPOINT_LIST + "?access_key=" + ACCESS_KEY,
                HttpMethod.GET,
                HttpEntity.EMPTY,
                Currencies.class);
        Assert.assertNotNull(response);
        Assert.assertEquals(response.getStatusCode(), HttpStatus.OK);
        Assert.assertNotNull(response.getBody());
    }

    @Autowired
    @Qualifier(INBOUND_CHANNEL_API_CURRENCYPLAYER_LIST)
    MessageChannel inboundChannel;

    @Autowired
    @Qualifier(OUTBOUND_CHANNEL_API_CURRENCYPLAYER_LIST)
    DirectChannel outboundChannel;

    //@Test
    public void testCurrencyPlayerIntegration() throws Exception {
        CountDownLatch latch = new CountDownLatch(1);
        AtomicBoolean replied = new AtomicBoolean(false);
        outboundChannel.subscribe(message -> {
            replied.compareAndSet(false, true);
            latch.countDown();
        });
        inboundChannel.send(new GenericMessage<>(new Date()));
        latch.await(3000, TimeUnit.MILLISECONDS);
        Assert.assertTrue(replied.get());
    }
}
