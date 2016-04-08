package com.zooplus.openexchange.controllers.v1;

public abstract class Version {
    public final static String API_PATH_V1 = "/api/v1";

    // categories
    public final static String USERS_ENDPOINT = API_PATH_V1 + "/users";
    public final static String ADMIN_ENDPOINT = API_PATH_V1 + "/admin";

    // methods
    public final static String STATUS_PATH = ADMIN_ENDPOINT + "/status";
    public final static String USER_REGISTRATION_PATH = USERS_ENDPOINT + "/register";
    public final static String USER_SESSION_DETAILS_PATH = USERS_ENDPOINT + "/sessionDetails";
    public final static String USER_LOGIN_PATH = USERS_ENDPOINT + "/login";
    public final static String USER_LOGOUT_PATH = USERS_ENDPOINT + "/logout";
}
