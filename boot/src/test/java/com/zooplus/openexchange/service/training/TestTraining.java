package com.zooplus.openexchange.service.training;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class TestTraining {
    @Test
    public void testStringReplacement() throws Exception {
        assertEquals(new StringContent(" Mr.  John   Smith  ").replace().get(), "Mr.%20John%20Smith");
        assertEquals(new StringContent("     ").replace().get(), "");
        assertEquals(new StringContent("abcd").replace().get(), "abcd");
        assertEquals(new StringContent("abcd  ").replace().get(), "abcd");
    }
}
