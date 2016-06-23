package com.zooplus.openexchange;

import org.springframework.boot.Banner;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableEurekaClient
public class Starter {
    public static void main(String... args) throws Exception {
        new SpringApplicationBuilder()
                .bannerMode(Banner.Mode.OFF)
                .sources(Starter.class)
                .run(args);
    }
}