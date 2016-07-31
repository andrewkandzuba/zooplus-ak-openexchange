package com.zooplus.openexchange.services.discovery;

import org.springframework.cloud.client.ServiceInstance;
import java.util.List;

public interface Discovery {
    List<ServiceInstance> getInstances();
}
