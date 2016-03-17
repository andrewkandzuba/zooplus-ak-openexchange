package com.zooplus.openexchange.integrations.configuration;

import org.springframework.cloud.service.UriBasedServiceInfo;

public class PlainServiceInfo extends UriBasedServiceInfo {
    public PlainServiceInfo(String id, String uri) {
        super( id, uri );
    }
}
