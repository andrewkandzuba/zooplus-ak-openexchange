package com.zooplus.openexchange.tests.integration;

import com.zooplus.openexchange.services.external.currencylayer.api.CurrencyLayerApi;
import com.zooplus.openexchange.services.external.currencylayer.impl.CurrencyLayerDAO;
import com.zooplus.openexchange.starters.IntegrationTestStarter;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(IntegrationTestStarter.class)
@ActiveProfiles("integration")
public class TestCurrencyApiCache {
    @Autowired
    private CurrencyLayerApi currencyLayerApiGateway;

    @Autowired
    private CurrencyLayerDAO layerDAO;

    @Test
    public void testCacheableResponse() throws Exception {
        //currencyLayerApiGateway.getCurrenciesList(10);
        //currencyLayerApiGateway.getCurrenciesList(10);
        layerDAO.getCurrenciesListCached(10);
        layerDAO.getCurrenciesListCached(10);
    }
}
