package com.zooplus.openexchange.controllers.v1;

public interface CasProtocol {
    // Endpoint
    String API_PATH_V1 = "/api/v1/";
    
    // resources
    // users management
    String USER_RESOURCE = "user";

    // healthcheck and instance information
    String HEALTH_RESOURCE = "health";
    String INFO_RESOURCE = "info";

    // session management
    String LOGIN_RESOURCE = "login";
    String LOGOUT_RESOURCE = "logout";
    String SESSION_RESOURCE = "session";
}
