package com.zooplus.openexchange.boot;


import com.zooplus.openexchange.platform.cf.CfConfigurationOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.app.ApplicationInstanceInfo;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.net.InetSocketAddress;

@Component
@Configuration
public class CFInstance implements Instance {
    private String id;
    private InetSocketAddress address;

    @Autowired(required = true)
    private ApplicationInstanceInfo instanceInfo;

    @PostConstruct
    private void init(){
        this.id = instanceInfo.getInstanceId();
        this.address = InetSocketAddress.createUnresolved(
                (String) instanceInfo.getProperties().get(CfConfigurationOptions.CF_INSTANCE_HOST),
                (Integer) instanceInfo.getProperties().get(CfConfigurationOptions.CF_INSTANCE_PORT));
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public InetSocketAddress getAddress() {
        return address;
    }
}
