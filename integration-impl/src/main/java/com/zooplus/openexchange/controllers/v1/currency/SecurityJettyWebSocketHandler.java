package com.zooplus.openexchange.services.security;

import com.zooplus.openexchange.clients.RestClient;
import com.zooplus.openexchange.controllers.JettyWebSocketHandler;
import com.zooplus.openexchange.controllers.MessageProcessor;
import com.zooplus.openexchange.services.discovery.Discovery;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.util.Optional;

public class SecurityJettyWebSocketHandler  extends JettyWebSocketHandler {
    private final static String X_AUTH_TOKEN_HEADER = "x-auth-token";
    private final Discovery discovery;

    public SecurityJettyWebSocketHandler(Discovery discovery, MessageProcessor... processors) {
        super(processors);
        this.discovery = discovery;
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        String authToken = session.getHandshakeHeaders().toSingleValueMap().get(X_AUTH_TOKEN_HEADER);
        if(authToken == null) {
            sendErrorMessageToSession(session, "Unauthorized request: " + X_AUTH_TOKEN_HEADER + " header is missing");
        } else {
            Optional<ServiceInstance> si = discovery.getInstances().stream().findFirst();
            if(!si.isPresent()){
                sendErrorMessageToSession(session, "Security verification service is not available");
            } else {
                RestClient restClient = new RestClient(si.get().getHost(), si.get().getPort());
                //restClient.exchange()
            }
            // @ToDo: verify authentication token here via frontend (cas) service
        }
        super.handleTextMessage(session, message);
    }
}
