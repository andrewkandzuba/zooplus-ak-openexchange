package com.zooplus.openexchange.service;

import org.springframework.boot.Banner;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = { "com.zooplus.openexchange.service.configuration", "com.zooplus.openexchange.service.controllers" })
public class Starter {
    public static void main(String... args) throws Exception {
        new SpringApplicationBuilder()
                .bannerMode(Banner.Mode.OFF)
                .sources(Starter.class)
                .run(args);
    }
}
