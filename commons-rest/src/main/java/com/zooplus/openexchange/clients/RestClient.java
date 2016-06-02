package com.zooplus.openexchange.clients;

import javafx.util.Pair;
import org.springframework.http.*;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.ResponseErrorHandler;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.Map;
import java.util.Optional;

public class RestClient {

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
        return restTemplate.exchange(
                endPoint + "/" + path,
                method,
                entity,
                responseClazz);
    }

    @SafeVarargs
    public static HttpHeaders build(Pair<String, String>... pairs){
        return build(new HttpHeaders(), pairs);
    }

    @SafeVarargs
    public static HttpHeaders build(HttpHeaders headers, Pair<String, String>... pairs){
        HttpHeaders newHeaders = new HttpHeaders();
        for(Map.Entry<String, String> pair : headers.toSingleValueMap().entrySet()){
            newHeaders.add(pair.getKey(), pair.getValue());
        }
        for(Pair pair : pairs){
            newHeaders.add((String) pair.getKey(), (String) pair.getValue());
        }
        return HttpHeaders.readOnlyHttpHeaders(newHeaders);
    }
}
