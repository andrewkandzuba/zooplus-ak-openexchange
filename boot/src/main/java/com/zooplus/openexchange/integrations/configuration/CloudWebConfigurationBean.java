package com.zooplus.openexchange.integrations.configuration;

import com.zooplus.openexchange.boot.Instance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.embedded.ConfigurableEmbeddedServletContainer;
import org.springframework.boot.context.embedded.EmbeddedServletContainerCustomizer;
import org.springframework.stereotype.Component;

@Component
public class CloudWebConfigurationBean implements EmbeddedServletContainerCustomizer {
    @Autowired
    private Instance instance;

    @Override
    public void customize(ConfigurableEmbeddedServletContainer container) {
        container.setPort(instance.getAddress().getPort());
    }
}
