package com.zooplus.openexchange.controllers.v1.currency;

import com.zooplus.openexchange.controllers.MessageProcessor;
import com.zooplus.openexchange.integrations.gateways.CurrenciesGateway;
import com.zooplus.openexchange.protocol.ws.v1.CurrenciesListRequest;
import com.zooplus.openexchange.protocol.ws.v1.CurrenciesListResponse;
import com.zooplus.openexchange.utils.MessageConvetor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketSession;

@Component
class CurrenciesListRequestMessagesProcessor implements MessageProcessor {
    @Autowired
    CurrenciesGateway currenciesGateway;

    @Override
    public void handle(WebSocketSession session, Object message) throws Exception {
        CurrenciesListResponse response = new CurrenciesListResponse();
        response.setCurrencies(currenciesGateway.getCurrencies());
        session.sendMessage(MessageConvetor.to(response, CurrenciesListResponse.class));
    }

    @Override
    public boolean supports(Class<?> payloadClass) {
        return payloadClass.equals(CurrenciesListRequest.class);
    }
}
