package com.zooplus.openexchange.services.stubs.discovery;

import com.zooplus.openexchange.services.discovery.Discovery;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

@Component
public class LocalDiscoveryStub implements Discovery {
    @Override
    public List<ServiceInstance> getInstances() {
        return Collections.emptyList();
    }
}
