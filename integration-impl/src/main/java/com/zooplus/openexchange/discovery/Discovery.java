package com.zooplus.openexchange.discovery;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

@Component
public class Discovery {

    @Autowired
    private DiscoveryClient discoveryClient;

    public List<ServiceInstance> discoverOf(String serviceType){
        List<ServiceInstance> instances = discoveryClient.getInstances(serviceType);
        Collections.shuffle(instances);
        return Collections.unmodifiableList(instances);
    }
}
