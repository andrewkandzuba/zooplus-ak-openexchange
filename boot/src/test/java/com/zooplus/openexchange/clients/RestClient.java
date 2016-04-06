package com.zooplus.openexchange.clients;

import javafx.util.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.ResponseErrorHandler;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class RestClient {
    private final static Logger logger = LoggerFactory.getLogger(RestClient.class);
    private final String endPoint;
    private final RestTemplate restTemplate;

    public RestClient(String host, int port) {
        this.endPoint = String.format("http://%s:%s", host, port);
        this.restTemplate = new RestTemplate();
        this.restTemplate.setErrorHandler(new ResponseErrorHandler() {
            @Override
            public boolean hasError(ClientHttpResponse clientHttpResponse) throws IOException {
                return clientHttpResponse.getStatusCode() != HttpStatus.OK;
            }

            @Override
            public void handleError(ClientHttpResponse clientHttpResponse) throws IOException {
                // ToDo: do something useful here
            }
        });
    }

    public <T, K> ResponseEntity<T> exchange(String path,
                                          HttpMethod method,
                                          HttpHeaders headers,
                                          Optional<K> body,
                                          Class<T> responseClazz) {
        // Make entity
        HttpEntity<K> entity = body.isPresent() ? new HttpEntity<>(body.get(), headers) : new HttpEntity<>(headers);
        // Send request
        ResponseEntity<T> resp = restTemplate.exchange(
                endPoint + "/" + path,
                method,
                entity,
                responseClazz);

        // trace response
        logger.debug("----------------------------------------------------------------------");
        logger.debug("HttpStatus: %s",  resp.getStatusCode());
        for (Map.Entry<String, List<String>> header : resp.getHeaders().entrySet()) {
            StringBuilder sb = new StringBuilder();
            sb.append(String.format("Header : %s ( ", header.getKey()));
            for (String value : header.getValue()) {
                sb.append(String.format(" %s ", value));
            }
            sb.append(" )");
            logger.debug(sb.toString());
        }
        if(resp.getStatusCode() == HttpStatus.OK) {
            logger.debug(resp.getBody().toString());
        }
        logger.debug("----------------------------------------------------------------------");

        return resp;
    }

    @SafeVarargs
    public static HttpHeaders headersFrom(Pair<String, String>... pairs){
        HttpHeaders headers = new HttpHeaders();
        for(Pair pair : pairs){
            headers.add((String)pair.getKey(), (String)pair.getValue());
        }
        return HttpHeaders.readOnlyHttpHeaders(headers);
    }
}
