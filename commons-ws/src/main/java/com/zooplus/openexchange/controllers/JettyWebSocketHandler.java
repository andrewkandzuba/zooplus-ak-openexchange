package com.zooplus.openexchange.controllers;

import com.zooplus.openexchange.protocol.ws.ErrorMessage;
import com.zooplus.openexchange.protocol.ws.MessageWrapper;
import com.zooplus.openexchange.utils.MessageConvetor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.Arrays;
import java.util.concurrent.atomic.AtomicBoolean;

public class JettyWebSocketHandler extends TextWebSocketHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(JettyWebSocketHandler.class);
    private final MessageProcessor[] processors;

    public JettyWebSocketHandler(MessageProcessor... processors) {
        this.processors = processors;
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        LOGGER.info("message received: " + message.getPayload());
        MessageWrapper wrapper = MessageConvetor.from(message);
        Class clazz = Class.forName(wrapper.getPayloadClassName());
        Object origin = MessageConvetor.from(wrapper.getPayloadContent(), clazz);
        if (origin instanceof ErrorMessage) {
            handleClientErrorMessage(session, (ErrorMessage) origin);
            return;
        }
        AtomicBoolean handled = new AtomicBoolean(false);
        Arrays.stream(processors).forEach(mp -> {
            try {
                handled.compareAndSet(false, mp.onMessage(session, origin, clazz));
            } catch (Exception e) {
                sendErrorMessageToSession(session, String.format("Error has occurred during message handling process %s  %s  ", session.getId(), e.getMessage()));
            }
        });
        if (!handled.get()){
            String errorDescription = "Unexpected WebSocket message type: " + clazz + " session: " + session.getId();
            LOGGER.error(errorDescription);
            ErrorMessage errorMessage = new ErrorMessage();
            errorMessage.setErrorCode(0);
            errorMessage.setErrorMessage(errorDescription);
            try {
                session.sendMessage(MessageConvetor.to(errorMessage, ErrorMessage.class));
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        }
    }

    public void handleClientErrorMessage(WebSocketSession session, ErrorMessage e) {
        LOGGER.info("Handle error message: [sessionId: {}, errorCode: {}, errorMessage: {}", session.getId(), e.getErrorCode(), e.getErrorMessage());
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        LOGGER.info("Connected ... " + session.getId());
    }

    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
        LOGGER.error("Error has occurred at sender " + session, exception);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        LOGGER.info(String.format("Session: %s has been closed due to: %s", session.getId(), status.getReason()));
    }

    protected static void sendErrorMessageToSession(WebSocketSession session, String errorDescription){
        LOGGER.error(errorDescription);
        ErrorMessage errorMessage = new ErrorMessage();
        errorMessage.setErrorCode(0);
        errorMessage.setErrorMessage(errorDescription);
        try {
            session.sendMessage(MessageConvetor.to(errorMessage, ErrorMessage.class));
        } catch (Exception e1) {
            e1.printStackTrace();
        }
    }
}
