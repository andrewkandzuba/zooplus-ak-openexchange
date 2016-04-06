package com.zooplus.openexchange.starters;

import com.zooplus.openexchange.protocol.v1.Status;
import com.zooplus.openexchange.services.StatusService;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.integration.annotation.IntegrationComponentScan;

@SpringBootApplication(
        scanBasePackages = {
                "com.zooplus.openexchange.integrations"
        })
@IntegrationComponentScan("com.zooplus.openexchange.integrations")
public class LogicStarter {
    @Bean
    StatusService getStatusService() {
        return () -> {
            Status s = new Status();
            s.setHost("localhost");
            s.setInstanceId("id-1");
            s.setPort(8888);
            return s;
        };
    }
}
