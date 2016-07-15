package com.zooplus.openexchange.controllers;

import com.zooplus.openexchange.protocol.ws.v1.ErrorMessage;
import com.zooplus.openexchange.utils.MessageConvetor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

public class SecurityJettyWebSocketHandler  extends JettyWebSocketHandler {
    private final static String X_AUTH_TOKEN_HEADER = "x-auth-token";
    private static final Logger LOGGER = LoggerFactory.getLogger(JettyWebSocketHandler.class);

    public SecurityJettyWebSocketHandler(MessageProcessor... processors) {
        super(processors);
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        String authToken = session.getHandshakeHeaders().toSingleValueMap().get(X_AUTH_TOKEN_HEADER);

        if(authToken == null) {
            String errorDescription = "Unauthorized request: " + X_AUTH_TOKEN_HEADER + " header is missing";
            LOGGER.error(errorDescription);
            ErrorMessage errorMessage = new ErrorMessage();
            errorMessage.setErrorCode(0);
            errorMessage.setErrorMessage(errorDescription);
            try {
                session.sendMessage(MessageConvetor.to(errorMessage, ErrorMessage.class));
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        } else {
            // @ToDo: verify authentication token here via frontend (cas) service
        }
        super.handleTextMessage(session, message);
    }
}
