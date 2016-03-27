package com.zooplus.openexchange.protocol;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zooplus.openexchange.protocol.v1.Currencies;
import com.zooplus.openexchange.protocol.v1.Quotes;
import org.apache.commons.io.IOUtils;
import org.junit.Assert;
import org.junit.Test;

public class TestCurrencyApiProtocol {
    @Test
    public void testListSuccess() throws Exception {
        String json = IOUtils.toString(this.getClass().getClassLoader().getResourceAsStream("responses/v1/list.success.json"));
        Currencies ob = new ObjectMapper().readValue(json, Currencies.class);

        Assert.assertNotNull(ob);
        Assert.assertTrue(ob.getSuccess());
        Assert.assertNull(ob.getError());
        Assert.assertEquals(ob.getTerms(), "https://currencylayer.com/terms");
        Assert.assertEquals(ob.getPrivacy(), "https://currencylayer.com/privacy");
        Assert.assertEquals(ob.getCurrencies().size(), 168);
        Assert.assertEquals(ob.getCurrencies().get("BZD"), "Belize Dollar");
    }

    @Test
    public void testLiveSuccess() throws Exception {
        String json = IOUtils.toString(this.getClass().getClassLoader().getResourceAsStream("responses/v1/live.success.json"));
        Quotes ob = new ObjectMapper().readValue(json, Quotes.class);

        Assert.assertNotNull(ob);
        Assert.assertTrue(ob.getSuccess());
        Assert.assertNull(ob.getError());
        Assert.assertEquals(ob.getTerms(), "https://currencylayer.com/terms");
        Assert.assertEquals(ob.getPrivacy(), "https://currencylayer.com/privacy");
        Assert.assertFalse(ob.getHistorical());
        Assert.assertNull(ob.getDate());
        Assert.assertEquals(ob.getTimestamp().longValue(), 1458216671L);
        Assert.assertEquals(ob.getSource(), "USD");
        Assert.assertEquals(ob.getQuotes().size(), 168);
        Assert.assertEquals(ob.getQuotes().get("USDAUD"), 1.313802);
    }

    @Test
    public void testHistoricalSuccess() throws Exception {
        String json = IOUtils.toString(this.getClass().getClassLoader().getResourceAsStream("responses/v1/historical.success.json"));
        Quotes ob = new ObjectMapper().readValue(json, Quotes.class);

        Assert.assertNotNull(ob);
        Assert.assertTrue(ob.getSuccess());
        Assert.assertNull(ob.getError());
        Assert.assertEquals(ob.getTerms(), "https://currencylayer.com/terms");
        Assert.assertEquals(ob.getPrivacy(), "https://currencylayer.com/privacy");
        Assert.assertTrue(ob.getHistorical());
        Assert.assertEquals(ob.getDate(), "2016-03-01");
        Assert.assertEquals(ob.getTimestamp().longValue(), 1456876799L);
        Assert.assertEquals(ob.getSource(), "USD");
        Assert.assertEquals(ob.getQuotes().size(), 168);
        Assert.assertEquals(ob.getQuotes().get("USDAUD"), 1.392835);
    }

    @Test
    public void testListErrors() throws Exception {
        String json = IOUtils.toString(this.getClass().getClassLoader().getResourceAsStream("responses/v1/error.json"));
        Currencies ob = new ObjectMapper().readValue(json, Currencies.class);
        Assert.assertNotNull(ob);
        Assert.assertFalse(ob.getSuccess());
        Assert.assertNotNull(ob.getError());
        Assert.assertEquals(ob.getError().getCode().longValue(), 104);
        Assert.assertEquals(ob.getError().getInfo(), "Your monthly usage limit has been reached. Please upgrade your registration plan.");
    }

    @Test
    public void testQuotesErrors() throws Exception {
        String json = IOUtils.toString(this.getClass().getClassLoader().getResourceAsStream("responses/v1/error.json"));
        Quotes ob = new ObjectMapper().readValue(json, Quotes.class);
        Assert.assertNotNull(ob);
        Assert.assertFalse(ob.getSuccess());
        Assert.assertNotNull(ob.getError());
        Assert.assertEquals(ob.getError().getCode().longValue(), 104);
        Assert.assertEquals(ob.getError().getInfo(), "Your monthly usage limit has been reached. Please upgrade your registration plan.");
    }
}
