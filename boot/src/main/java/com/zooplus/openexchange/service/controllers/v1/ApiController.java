package com.zooplus.openexchange.service.controllers.v1;

public interface ApiController {
    String API_PATH = "/api/v1";

    String AUTHENTICATE_ENDPOINT = API_PATH + "/authenticate";
    String USERS_ENDPOINT = API_PATH + "/users";
    String ADMIN_ENDPOINT = API_PATH + "/admin";

    // methods
    String STATUS_PATH = ADMIN_ENDPOINT + "/status";
    String USER_REGISTRATION_PATH = USERS_ENDPOINT + "/registration";
}
