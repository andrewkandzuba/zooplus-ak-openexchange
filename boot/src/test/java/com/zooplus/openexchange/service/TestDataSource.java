package com.zooplus.openexchange.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(Starter.class)
@WebIntegrationTest
@ActiveProfiles("development")
public class TestDataSource {

    @Test
    public void testDataSourceAccess() throws Exception {
        //Assert.assertNotNull(dataSource);
    }
}
