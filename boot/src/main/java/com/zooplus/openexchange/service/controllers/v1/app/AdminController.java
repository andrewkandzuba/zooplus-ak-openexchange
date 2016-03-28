package com.zooplus.openexchange.service.controllers.v1.app;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zooplus.openexchange.protocol.v1.Status;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

import static com.zooplus.openexchange.service.controllers.v1.Constants.*;

@RestController
@RequestMapping("/" + API  + "/" + VERSION + "/" + ADMIN + "/" + STATUS)
class AdminController {
    private final static String VCAP_APPLICATION = "VCAP_APPLICATION";

    @Autowired
    private Environment environment;

    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    Status get() throws IOException {
        return new ObjectMapper().readValue(environment.getProperty(VCAP_APPLICATION), Status.class);
    }

    @ResponseStatus(value= HttpStatus.INTERNAL_SERVER_ERROR, reason="Unable to parse application's info")
    @ExceptionHandler({IOException.class, NullPointerException.class})
    public void errorHandler() {
    }
}
