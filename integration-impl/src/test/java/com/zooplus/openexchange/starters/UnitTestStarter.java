package com.zooplus.openexchange.starters;

import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(
        scanBasePackages = {
                "com.zooplus.openexchange.controllers.v1",
                "com.zooplus.openexchange.services.external.currencylayer.api",
                "com.zooplus.openexchange.services.external.currencylayer.stub",
                "com.zooplus.openexchange.services.discovery"
        })
public class UnitTestStarter {
}
