package com.zooplus.openexchange.starters;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.integration.annotation.IntegrationComponentScan;

@SpringBootApplication(
        scanBasePackages = {
                "com.zooplus.openexchange.controllers.v1",
                "com.zooplus.openexchange.integrations.stubs",
                "com.zooplus.openexchange.integrations.gateway",
                "com.zooplus.openexchange.integrations.services",
        })
@IntegrationComponentScan("com.zooplus.openexchange.integrations")
public class ApiStarter {
}
