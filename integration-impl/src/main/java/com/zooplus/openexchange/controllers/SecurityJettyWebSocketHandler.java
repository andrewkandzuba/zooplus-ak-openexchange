package com.zooplus.openexchange.controllers;

import com.zooplus.openexchange.services.security.SecurityTokenValidator;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

public class SecurityJettyWebSocketHandler extends JettyWebSocketHandler {
    private final static String X_AUTH_TOKEN_HEADER = "x-auth-token";
    private final SecurityTokenValidator securityTokenValidator;

    public SecurityJettyWebSocketHandler(SecurityTokenValidator securityTokenValidator, MessageProcessor... processors) {
        super(processors);
        this.securityTokenValidator = securityTokenValidator;
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        String authToken = session.getHandshakeHeaders().toSingleValueMap().get(X_AUTH_TOKEN_HEADER);
        if (authToken == null) {
            sendErrorMessageToSession(session, "Unauthorized request: " + X_AUTH_TOKEN_HEADER + " header is missing");
        } else if (!securityTokenValidator.isValid(authToken)) {
            sendErrorMessageToSession(session, "Security verification service is not available");
        } else {
            super.handleTextMessage(session, message);
        }
    }
}
