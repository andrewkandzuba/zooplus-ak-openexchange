package com.zooplus.openexchange.starters;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.PropertySource;

@SpringBootApplication(
        scanBasePackages = {
                "com.zooplus.openexchange"
        })
@PropertySource("classpath:config/environment-test.properties")
@EnableEurekaClient
public class IntegrationStarter {
}
