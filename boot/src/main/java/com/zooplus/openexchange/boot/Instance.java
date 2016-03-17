package com.zooplus.openexchange.boot;

import java.net.InetSocketAddress;

/**
 * Defines behavior of application instance
 */
public interface Instance {
    /**
     * Unique application's instance Id
     * @return UUID
     */
    String getId();

    /**
     * Instance access address (host and port). In case of cloud only port is essential.
     * @return InetSocketAddress
     */
    InetSocketAddress getAddress();
}
