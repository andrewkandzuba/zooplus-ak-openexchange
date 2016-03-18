package com.zooplus.openexchange.configuration;

import org.springframework.context.annotation.Bean;

import javax.sql.DataSource;

/*@Configuration
@Profile("local")*/
public class LocalApplicationConfiguration {
    @Bean
    public DataSource dataSource(){
        //jdbc:hsqldb:mem:testdb
        /*EmbeddedDatabaseBuilder builder = new EmbeddedDatabaseBuilder();
        return builder.setType(EmbeddedDatabaseType.HSQL)
                .addScript("db/hsqldb/db.sql")
                .build();*/
        return null;
    }
}
