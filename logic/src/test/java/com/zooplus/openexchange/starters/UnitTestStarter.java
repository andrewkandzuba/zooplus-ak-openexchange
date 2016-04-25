package com.zooplus.openexchange.starters;

import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(
        scanBasePackages = {
                "com.zooplus.openexchange.controllers.v1",
                "com.zooplus.openexchange.integrations.configurations",
                "com.zooplus.openexchange.integrations.stubs"
        })
public class UnitTestStarter {
}
