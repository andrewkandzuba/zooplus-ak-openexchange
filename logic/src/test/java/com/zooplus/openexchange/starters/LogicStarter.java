package com.zooplus.openexchange.starters;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.integration.annotation.IntegrationComponentScan;

@SpringBootApplication(
        scanBasePackages = {
                "com.zooplus.openexchange.integrations.gateway",
                "com.zooplus.openexchange.integrations.services",
                "com.zooplus.openexchange.integrations.stubs"
        })
@IntegrationComponentScan("com.zooplus.openexchange.integrations")
public class LogicStarter {
}
