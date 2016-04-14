package com.zooplus.openexchange.starters;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.integration.annotation.IntegrationComponentScan;

@SpringBootApplication(
        scanBasePackages = {
                "com.zooplus.openexchange.integrations",
                "com.zooplus.openexchange.controllers.v1"
        })
@IntegrationComponentScan("com.zooplus.openexchange.integrations")
public class IntegrationStarter {
}
