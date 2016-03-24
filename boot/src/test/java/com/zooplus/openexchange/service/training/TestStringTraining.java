package com.zooplus.openexchange.service.training;

import org.apache.commons.lang.StringUtils;
import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class TestStringTraining {
    @Test
    public void testStringReplacement() throws Exception {
        assertEquals(new StringContent(" Mr.  John   Smith  ").replace().get(), "Mr.%20John%20Smith");
        assertEquals(new StringContent("     ").replace().get(), "");
        assertEquals(new StringContent("abcd").replace().get(), "abcd");
        assertEquals(new StringContent("abcd  ").replace().get(), "abcd");
    }

    @Test
    public void testStringRotaion() throws Exception {
        Assert.assertTrue(StringUtils.contains("aaabbcc", "abb"));
        Assert.assertTrue(StringContent.from("waterbottle").isRotationOf("erbottlewat"));
        Assert.assertTrue(StringContent.from("wwwe").isRotationOf("ewww"));
    }
}
