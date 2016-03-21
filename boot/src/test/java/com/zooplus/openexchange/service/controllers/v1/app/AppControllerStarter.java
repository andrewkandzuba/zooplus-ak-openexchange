package com.zooplus.openexchange.service.controllers.v1.app;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;

@SpringBootApplication(scanBasePackages = {"com.zooplus.openexchange.service.controllers.v1.app"})
@PropertySource("classpath:config/environment-development.properties")
@Profile("development")
public class AppControllerStarter {
}
