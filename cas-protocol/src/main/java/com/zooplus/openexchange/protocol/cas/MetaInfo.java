package com.zooplus.openexchange.protocol.cas;

import java.util.HashMap;
import java.util.Map;

public abstract class MetaInfo {
    public final static Map<String, String> resources = new HashMap<>();
    public final static String LOGIN_RESOURCE = "/api/cas/login";
    public final static String LOGOUT_RESOURCE = "/api/cas/logout";
    public final static String USERS_RESOURCE = "/api/cas/users";
    public final static String SESSION_RESOURCE = "/api/cas/session";
    public final static String STATUS_RESOURCE = "/api/cas/status";

    static {
        resources.put("LoginRequest", LOGIN_RESOURCE);
        resources.put("LogoutRequest", LOGOUT_RESOURCE);
        resources.put("RegistrationRequest", USERS_RESOURCE);
        resources.put("SessionDetailsRequest", SESSION_RESOURCE);
        resources.put("StatusRequest", STATUS_RESOURCE);
    }
}
