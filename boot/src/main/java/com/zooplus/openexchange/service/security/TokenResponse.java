package com.zooplus.openexchange.service.security;

import com.fasterxml.jackson.annotation.JsonProperty;

class TokenResponse {
    @JsonProperty
    private String token;

    public TokenResponse() {
    }

    TokenResponse(String token) {
        this.token = token;
    }
}