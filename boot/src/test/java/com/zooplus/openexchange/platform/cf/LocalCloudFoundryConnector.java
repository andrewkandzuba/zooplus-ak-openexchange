package com.zooplus.openexchange.platform.cf;

import com.google.common.base.Throwables;
import com.netflix.config.ConcurrentCompositeConfiguration;
import com.netflix.config.ConfigurationManager;
import com.zooplus.openexchange.integrations.configuration.PlainServiceInfo;
import org.apache.commons.configuration.AbstractConfiguration;
import org.apache.commons.lang.StringUtils;
import org.springframework.cloud.CloudConnector;
import org.springframework.cloud.app.ApplicationInstanceInfo;
import org.springframework.cloud.app.BasicApplicationInstanceInfo;
import org.springframework.cloud.service.ServiceInfo;
import org.springframework.cloud.service.common.OracleServiceInfo;

import java.io.FileNotFoundException;
import java.net.URL;
import java.util.*;

import static org.apache.commons.lang.StringUtils.EMPTY;

public class LocalCloudFoundryConnector implements CloudConnector {

    private static final String VCAP_APPLICATION = "VCAP_APPLICATION";
    private static final String SERVICE_PREFIX = "service";
    private static final String LOCAL_CLOUD_PROPERTIES = "local.cloud.properties";

    private final Map<String, Object> props = new HashMap<>();
    private BasicApplicationInstanceInfo instanceInfo;
    private List<ServiceInfo> services;

    public LocalCloudFoundryConnector() throws Exception {
        URL resource = Thread.currentThread().getContextClassLoader().getResource( LOCAL_CLOUD_PROPERTIES );
        ConcurrentCompositeConfiguration ccc = (ConcurrentCompositeConfiguration) ConfigurationManager.getConfigInstance();
        if ( resource != null ) {
            try {
                AbstractConfiguration localCfg = ConfigurationManager.getConfigFromPropertiesFile( resource );
                ccc.addConfiguration( localCfg );
            }
            catch ( FileNotFoundException e ) {
                throw Throwables.propagate(e);
            }
        }
        init( ccc );
    }

    @Override
    public boolean isInMatchingCloud() {
        return System.getenv(VCAP_APPLICATION) == null;
    }

    @Override
    public ApplicationInstanceInfo getApplicationInstanceInfo() {
        return instanceInfo;
    }

    @Override
    public List<ServiceInfo> getServiceInfos() {
        return services;
    }

    private void init(final AbstractConfiguration cfg) throws Exception {
        List<ServiceInfo> b = new ArrayList<>();
        Iterator<String> keys = cfg.getKeys( SERVICE_PREFIX );
        while ( keys.hasNext() ) {
            String key = keys.next();
            String val = cfg.getString( key );
            if (key.contains("oracle")) {
                b.add( new OracleServiceInfo( key.substring( SERVICE_PREFIX.length() + 1 ) , val ) );
            } else {
                b.add( new PlainServiceInfo( key.substring( SERVICE_PREFIX.length() + 1 ) , val ) );
            }
            props.put( key, val );
        }
        int cfPort = randPort(1024, Short.MAX_VALUE);
        System.setProperty("server.port", String.valueOf(cfPort));
        props.put(CfConfigurationOptions.CLOUD_INSTANCE_INDEX.replace("cloud.application.", EMPTY), 0);
        props.put(CfConfigurationOptions.CF_SPACE_NAME.replace("cloud.application.", EMPTY), StringUtils.lowerCase("test-space"));
        props.put(CfConfigurationOptions.CLOUD_APP_PORT.replace("cloud.application.", EMPTY), cfPort);
        props.put(CfConfigurationOptions.CLOUD_APP_HOST.replace("cloud.application.", EMPTY), "localhost");
        instanceInfo = new BasicApplicationInstanceInfo(UUID.randomUUID().toString(), "test-app", props);
        services = Collections.unmodifiableList(b);
    }

    private static int randPort(int min, int max) {
        return new Random().nextInt((max - min) + 1) + min;
    }
}