package com.zooplus.openexchange.service.controllers.v1;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.io.IOException;

public abstract class ApiController {
    public final static String API_PATH = "/api/v1";

    public final static String USERS_ENDPOINT = API_PATH + "/users";
    public final static String ADMIN_ENDPOINT = API_PATH + "/admin";

    // methods
    public final static String STATUS_PATH = ADMIN_ENDPOINT + "/status";
    public final static String USER_REGISTRATION_PATH = USERS_ENDPOINT + "/register";
    public final static String USER_HELLO_PATH = USERS_ENDPOINT + "/hello";
    public final static String USER_AUTHENTICATE_PATH = USERS_ENDPOINT + "/authenticate";

    @ResponseStatus(value= HttpStatus.INTERNAL_SERVER_ERROR, reason="Error occurred!")
    @ExceptionHandler({IOException.class, NullPointerException.class})
    public void errorHandler() {}
}
