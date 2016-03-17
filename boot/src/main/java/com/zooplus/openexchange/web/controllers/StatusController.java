package com.zooplus.openexchange.web.controllers;

import com.zooplus.openexchange.boot.Instance;
import com.zooplus.openexchange.protocol.Status;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class StatusController {
    @Autowired
    Instance instance;

    @RequestMapping("/status")
    @ResponseBody
    Status get() {
        Status status = new Status();
        status.setId(instance.getId());
        status.setIp(instance.getAddress().getHostName());
        status.setPort(instance.getAddress().getPort());
        return status;
    }
}
