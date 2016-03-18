package com.zooplus.openexchange.web.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zooplus.openexchange.protocol.v1.Status;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.io.IOException;

import static com.zooplus.openexchange.configuration.ConfigurationConstants.VCAP_APPLICATION;

@Controller
public class StatusController {
    @Autowired
    Environment environment;

    @RequestMapping("/status")
    @ResponseBody
    Status get() throws IOException {
        return new ObjectMapper().readValue(environment.getProperty(VCAP_APPLICATION), Status.class);
    }

    @ResponseStatus(value= HttpStatus.INTERNAL_SERVER_ERROR, reason="Unable to parse application's info")
    @ExceptionHandler({IOException.class, NullPointerException.class})
    public void errorHandler() {
    }
}
