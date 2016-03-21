package com.zooplus.openexchange.service.controllers.v1.subscription;

import com.zooplus.openexchange.service.data.domain.Subscriber;
import com.zooplus.openexchange.service.data.repositories.SubscriberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
public class SubscriptionController {
    public static final String SUBSCRIBES_PATH = "/v1/subscription/subscribes";
    @Autowired
    SubscriberRepository subscriberRepository;

    @RequestMapping(value = SUBSCRIBES_PATH, method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<Subscriber> subscribes(@RequestBody Subscriber subscriber) throws IOException {
        if(subscriberRepository.findByEmail(subscriber.getEmail())!=null){
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
        return new ResponseEntity<>(subscriberRepository.saveAndFlush(subscriber), HttpStatus.OK);
    }

    @ResponseStatus(value= HttpStatus.INTERNAL_SERVER_ERROR, reason="Error occurred!")
    @ExceptionHandler({IOException.class, NullPointerException.class})
    public void errorHandler() {
    }

}
