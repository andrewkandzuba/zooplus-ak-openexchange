package com.zooplus.openexchange.service.controllers.v1.admin;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zooplus.openexchange.protocol.v1.Status;
import com.zooplus.openexchange.service.controllers.v1.ApiController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@PreAuthorize("hasAuthority('ADMIN')")
class AdminController implements ApiController{
    private final static String VCAP_APPLICATION = "VCAP_APPLICATION";

    @Autowired
    private Environment environment;

    @RequestMapping(method = RequestMethod.GET, path = STATUS_PATH)
    @ResponseBody
    Status get() throws IOException {
        return new ObjectMapper().readValue(environment.getProperty(VCAP_APPLICATION), Status.class);
    }

    @ResponseStatus(value= HttpStatus.INTERNAL_SERVER_ERROR, reason="Unable to parse application's info")
    @ExceptionHandler({IOException.class, NullPointerException.class})
    public void errorHandler() {
    }
}
