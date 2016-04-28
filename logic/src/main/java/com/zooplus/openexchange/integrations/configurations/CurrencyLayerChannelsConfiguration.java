package com.zooplus.openexchange.integrations.configurations;

import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.IntegrationComponentScan;
import org.springframework.integration.config.EnableIntegration;

@Configuration
@IntegrationComponentScan("com.zooplus.openexchange.integrations.gateways")
@EnableIntegration
public class CurrencyLayerChannelsConfiguration {
}
