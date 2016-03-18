package com.zooplus.openexchange.service.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;

@Configuration
@Profile("development")
@PropertySource("classpath:config/environment-development.properties")
public class LocalApplicationConfiguration {
   /* @Bean
    public DataSource dataSource(){
        //jdbc:hsqldb:mem:testdb
        EmbeddedDatabaseBuilder builder = new EmbeddedDatabaseBuilder();
        return builder.setType(EmbeddedDatabaseType.HSQL)
                .addScript("db/hsqldb/db.sql")
                .build();
    }*/
}
