package com.zooplus.openexchange.service.controllers.v1.subscribers;

import com.zooplus.openexchange.service.data.domain.Subscriber;
import com.zooplus.openexchange.service.data.repositories.SubscriberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
public class SubscribersControl {
    @Autowired
    SubscriberRepository subscriberRepository;

    @RequestMapping(value = "/v1/users/subscribes", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<Subscriber> subscribes(@RequestBody Subscriber subscriber) throws IOException {

        return new ResponseEntity<Subscriber>(HttpStatus.BAD_REQUEST);
    }

    @ResponseStatus(value= HttpStatus.INTERNAL_SERVER_ERROR, reason="Error occurred!")
    @ExceptionHandler({IOException.class, NullPointerException.class})
    public void errorHandler() {
    }

}
