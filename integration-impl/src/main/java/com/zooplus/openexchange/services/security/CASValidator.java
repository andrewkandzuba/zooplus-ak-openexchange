package com.zooplus.openexchange.services.security;

import com.zooplus.openexchange.clients.RestClient;
import com.zooplus.openexchange.protocol.rest.v1.SessionDetailsResponse;
import com.zooplus.openexchange.services.discovery.Discovery;
import javafx.util.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.Optional;

import static com.zooplus.openexchange.controllers.v1.CasProtocol.API_PATH_V1;
import static com.zooplus.openexchange.controllers.v1.CasProtocol.SESSION_RESOURCE;

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
                                    API_PATH_V1 + SESSION_RESOURCE,
                                    HttpMethod.GET,
                                    RestClient.build(new Pair<>("x-auth-token", token)),
                                    SessionDetailsResponse.class);
            return sessionDetailsResponse.getStatusCode() == HttpStatus.OK;

        } catch (Throwable t){
            return false;
        }
    }
}
