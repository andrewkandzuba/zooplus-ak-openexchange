package com.zooplus.openexchange.service.controllers.v1.admin;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zooplus.openexchange.protocol.v1.Status;
import com.zooplus.openexchange.service.controllers.v1.ApiController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@PreAuthorize("hasAuthority('ROLE_ADMIN')")
class AdminController extends ApiController{
    private final static String VCAP_APPLICATION = "VCAP_APPLICATION";

    @Autowired
    private Environment environment;

    @RequestMapping(method = RequestMethod.GET, path = STATUS_PATH)
    @ResponseBody
    Status get() throws IOException {
        return new ObjectMapper().readValue(environment.getProperty(VCAP_APPLICATION), Status.class);
    }
}
