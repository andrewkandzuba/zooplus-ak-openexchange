package com.zooplus.openexchange.protocol;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zooplus.openexchange.protocol.v1.Status;
import org.apache.commons.io.IOUtils;
import org.junit.Assert;
import org.junit.Test;

public class TestVcapResponse {
    @Test
    public void testInstanceParse() throws Exception {
        String json = IOUtils.toString(this.getClass().getClassLoader().getResourceAsStream("responses/v1/vcap.application.content.json"));
        Status ob = new ObjectMapper().readValue(json, Status.class);

        Assert.assertNotNull(ob);
        Assert.assertEquals(ob.getInstanceId(), "7935a0789a204973ab70b6f01458b4f3");
        Assert.assertEquals(ob.getHost(), "0.0.0.0");
        Assert.assertEquals(ob.getPort().intValue(), 61334);
    }

}
