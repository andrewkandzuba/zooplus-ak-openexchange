package com.zooplus.openexchange.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zooplus.openexchange.protocol.cas.MetaInfo;
import com.zooplus.openexchange.protocol.cas.StatusResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/")
@PreAuthorize("permitAll()")
class ApplicationController {
    private final static String VCAP_APPLICATION = "VCAP_APPLICATION";

    @Autowired
    private Environment environment;

    @RequestMapping(method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            path = MetaInfo.STATUS_RESOURCE)
    @ResponseBody
    ResponseEntity<StatusResponse> status() throws IOException {
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
