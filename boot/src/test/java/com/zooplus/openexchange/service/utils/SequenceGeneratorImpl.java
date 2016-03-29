package com.zooplus.openexchange.service.utils;

import org.springframework.context.annotation.Configuration;

@Configuration
public class SequenceGeneratorImpl implements SequenceGenerator {
    private volatile long nextId = 0;

    @Override
    public long nextLong() {
        return ++nextId;
    }
}
