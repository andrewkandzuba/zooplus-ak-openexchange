package com.zooplus.openexchange.services.discovery;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

@Component
@EnableAsync
@EnableScheduling
public class EurekaDiscovery implements Discovery {
    private final AtomicReference<List<ServiceInstance>> instances = new AtomicReference<>();

    @Autowired
    private DiscoveryClient discoveryClient;

    @Value("${eureka.client.types}")
    private List<String> types;

    public List<ServiceInstance> getInstances() {
        instances.compareAndSet(null, instances.get());
        return Collections.unmodifiableList(instances.get());
    }

    @Scheduled(fixedRate = 5000)
    private void refresh(){
        instances.set(discoverAll());
    }

    private List<ServiceInstance> discoverAll() {
        List<ServiceInstance> instances = new ArrayList<>();
        types.forEach(type -> instances.addAll(discoveryClient.getInstances(type)));
        return instances;
    }
}
