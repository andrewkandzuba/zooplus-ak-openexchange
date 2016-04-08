package com.zooplus.openexchange.controllers.v1;

public class Version {
    public final static String API_PATH_V1 = "/api/v1";

    // categories
    public final static String CURRENCIES_ENDPOINT = "/currencies";
    public final static String TOPIC_ENDPOINT = "/topic";
    public final static String WS_ENDPOINT = "/ws";

    // methods
    public final static String CURRENCIES_LIST_API = CURRENCIES_ENDPOINT + "/list";
    public final static String CURRENCIES_LIST_TOPIC = TOPIC_ENDPOINT + CURRENCIES_LIST_API;
}
