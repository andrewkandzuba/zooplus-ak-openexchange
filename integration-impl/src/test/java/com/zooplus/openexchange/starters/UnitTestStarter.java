package com.zooplus.openexchange.starters;

import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(
        scanBasePackages = {
                "com.zooplus.openexchange.controllers",
                "com.zooplus.openexchange.services.stubs"
        })
public class UnitTestStarter {
}
