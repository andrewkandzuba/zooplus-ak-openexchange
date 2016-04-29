package com.zooplus.openexchange.starters;

import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(
        scanBasePackages = {
                "com.zooplus.openexchange.integrations.configurations",
                "com.zooplus.openexchange.integrations.api",
                "com.zooplus.openexchange.integrations.endpoints",
                "com.zooplus.openexchange.controllers.v1"
        })
public class IntegrationTestStarter {
}
