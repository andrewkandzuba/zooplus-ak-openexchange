package com.zooplus.openexchange.starters;

import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(
        scanBasePackages = {
                "com.zooplus.openexchange.controllers",
                "com.zooplus.openexchange.services.external.currencylayer.api",
                "com.zooplus.openexchange.services.external.currencylayer.impl",
                "com.zooplus.openexchange.services.discovery",
                "com.zooplus.openexchange.services.cache",
                "com.zooplus.openexchange.services.security"
        })
public class IntegrationTestStarter {
}
