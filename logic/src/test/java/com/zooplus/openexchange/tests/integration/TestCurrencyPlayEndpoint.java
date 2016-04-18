package com.zooplus.openexchange.tests.integration;

import com.zooplus.openexchange.protocol.integration.v1.Currencies;
import com.zooplus.openexchange.starters.IntegrationStarter;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Date;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import static com.zooplus.openexchange.integrations.api.CurrencyPlayerIntegrationFlow.INBOUND_CHANNEL_API_CURRENCYPLAYER_LIST;
import static com.zooplus.openexchange.integrations.api.CurrencyPlayerIntegrationFlow.OUTBOUND_CHANNEL_API_CURRENCYPLAYER_LIST;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(IntegrationStarter.class)
@WebIntegrationTest("server.port:0")
@ActiveProfiles("integration")
public class TestCurrencyPlayEndpoint {
    @Autowired
    @Qualifier(INBOUND_CHANNEL_API_CURRENCYPLAYER_LIST)
    private MessageChannel inboundChannel;

    @Autowired
    @Qualifier(OUTBOUND_CHANNEL_API_CURRENCYPLAYER_LIST)
    private DirectChannel outboundChannel;

    @Test
    public void testCurrencyPlayerIntegration() throws Exception {
        CountDownLatch latch = new CountDownLatch(1);
        AtomicBoolean replied = new AtomicBoolean(false);
        outboundChannel.subscribe(message -> {
            Currencies currencies = (Currencies) message.getPayload();
            Assert.assertTrue(currencies.getSuccess());
            Assert.assertTrue(currencies.getCurrencies().size() >  0);
            replied.compareAndSet(false, true);
            latch.countDown();
        });
        inboundChannel.send(new GenericMessage<>(new Date()));
        latch.await(3000, TimeUnit.MILLISECONDS);
        Assert.assertTrue(replied.get());
    }
}
