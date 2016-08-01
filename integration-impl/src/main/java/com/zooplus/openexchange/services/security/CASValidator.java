package com.zooplus.openexchange.services.security;

import com.zooplus.openexchange.clients.RestClient;
import com.zooplus.openexchange.protocol.cas.MetaInfo;
import com.zooplus.openexchange.protocol.cas.SessionDetailsResponse;
import com.zooplus.openexchange.services.discovery.Discovery;
import javafx.util.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class CASValidator implements SecurityTokenValidator {
    @Autowired
    private Discovery discovery;

    @Override
    public boolean isValid(String token) {
        Optional<ServiceInstance> casInstance = discovery.getInstances().stream().findFirst();
        if (!casInstance.isPresent()) {
            return false;
        }
        try {
            ResponseEntity<SessionDetailsResponse> sessionDetailsResponse =
                    new RestClient(casInstance.get().getHost(), casInstance.get().getPort())
                            .exchange(
                                    MetaInfo.SESSION_RESOURCE,
                                    HttpMethod.GET,
                                    RestClient.build(new Pair<>("x-auth-token", token)),
                                    SessionDetailsResponse.class);
            return sessionDetailsResponse.getStatusCode() == HttpStatus.OK;

        } catch (Throwable t){
            return false;
        }
    }
}
