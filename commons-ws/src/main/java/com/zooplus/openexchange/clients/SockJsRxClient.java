package com.zooplus.openexchange.clients;

import com.zooplus.openexchange.controllers.JettyWebSocketHandler;
import com.zooplus.openexchange.controllers.MessageProcessor;
import com.zooplus.openexchange.protocol.ws.v1.ErrorMessage;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.client.WebSocketClient;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.sockjs.client.RestTemplateXhrTransport;
import org.springframework.web.socket.sockjs.client.SockJsClient;
import org.springframework.web.socket.sockjs.client.Transport;
import org.springframework.web.socket.sockjs.client.WebSocketTransport;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

public class SockJsRxClient {
    private final WebSocketClient sockJsClient;
    public static Builder builder() {
        return new SockJsRxClient().new Builder();
    }

    private SockJsRxClient() {
        List<Transport> transports = new ArrayList<>(2);
        transports.add(new WebSocketTransport(new StandardWebSocketClient()));
        transports.add(new RestTemplateXhrTransport());
        this.sockJsClient = new SockJsClient(transports);
    }

    public ListenableFuture<WebSocketSession> doHandshake(String endpoint,
                                                          Optional<Consumer<ErrorMessage>> errorMessageHandler,
                                                          MessageProcessor... processors) {
        return sockJsClient.doHandshake(new JettyWebSocketHandler(processors) {
            @Override
            public void handleClientErrorMessage(WebSocketSession session, ErrorMessage e) {
                if (errorMessageHandler.isPresent()) {
                    errorMessageHandler.get().accept(e);
                }
            }
        }, endpoint);
    }

    public class Builder {
        private Builder() {
        }

        public SockJsRxClient get(){
            return SockJsRxClient.this;
        }
    }
}
