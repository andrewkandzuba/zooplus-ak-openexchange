package com.zooplus.openexchange.controllers.v1;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zooplus.openexchange.protocol.rest.v1.StatusResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

import static com.zooplus.openexchange.controllers.v1.Version.API_PATH_V1;
import static com.zooplus.openexchange.controllers.v1.Version.HEALTH_CHECK_RESOURCE;

@RestController
@RequestMapping(API_PATH_V1)
@PreAuthorize("permitAll()")
class ApplicationController {
    private final static String VCAP_APPLICATION = "VCAP_APPLICATION";

    @Autowired
    private Environment environment;

    @RequestMapping(method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            path = HEALTH_CHECK_RESOURCE)
    @ResponseBody
    ResponseEntity<StatusResponse> get() throws IOException {
        return new ResponseEntity<>(
                new ObjectMapper()
                        .readValue(environment.getProperty(VCAP_APPLICATION),
                        StatusResponse.class),
                HttpStatus.OK);
    }

    @ResponseStatus(value= HttpStatus.INTERNAL_SERVER_ERROR, reason="Error occurred!")
    @ExceptionHandler({IOException.class, NullPointerException.class})
    public void errorHandler() {}
}
