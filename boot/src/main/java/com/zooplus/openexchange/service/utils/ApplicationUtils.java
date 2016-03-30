package com.zooplus.openexchange.service.utils;

import java.util.UUID;

public interface ApplicationUtils {
    static String nextToken(){
        return UUID.randomUUID().toString();
    }
}
