package com.zooplus.openexchange.controllers.v1.currency;

import com.zooplus.openexchange.integrations.gateways.CurrenciesGateway;
import com.zooplus.openexchange.protocol.ws.v1.CurrenciesListRequest;
import com.zooplus.openexchange.protocol.ws.v1.CurrenciesListResponse;
import com.zooplus.openexchange.utils.MessageConvetor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

@Component
class CurrenciesWebSocketHandler extends TextWebSocketHandler {
    @Autowired
    CurrenciesGateway currenciesGateway;

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        // Parse message
        Object t = MessageConvetor.from(message.getPayload());

        // Handle response
        if(t instanceof CurrenciesListRequest){
            CurrenciesListResponse response = new CurrenciesListResponse();
            response.setCurrencies(currenciesGateway.getCurrencies());
            session.sendMessage(new TextMessage(MessageConvetor.to(response, CurrenciesListResponse.class)));
            return;
        }

        // Throws exception if no handlers found
        throw new Exception("Message not supported");
    }
}
