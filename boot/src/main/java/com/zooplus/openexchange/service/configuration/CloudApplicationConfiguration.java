package com.zooplus.openexchange.service.configuration;

import org.springframework.context.annotation.Bean;

import javax.sql.DataSource;

/*@Configuration
@Profile("cloud")*/
public class CloudApplicationConfiguration {
    @Bean
    public DataSource dataSource(){
        return null;
    }
}

