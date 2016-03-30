package com.zooplus.openexchange.service.utils;

import java.util.UUID;

public abstract class ApplicationUtils {
    public static volatile long nextId = 0;
    public static String nextToken(){
        return UUID.randomUUID().toString();
    }
}
