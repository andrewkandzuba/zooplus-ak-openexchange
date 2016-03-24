package com.zooplus.openexchange.service.controllers.v1.subscription;

import com.zooplus.openexchange.protocol.v1.Registrationrequest;
import com.zooplus.openexchange.protocol.v1.Registrationresponse;
import com.zooplus.openexchange.service.data.domain.User;
import com.zooplus.openexchange.service.data.repositories.SubscriberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
public class RegistrationController {
    public static final String REGISTRATION_REGISTER = "/v1/registration/register";
    public static final String REGISTRATION_AUTH = "/v1/registration/authorize";
    @Autowired
    SubscriberRepository subscriberRepository;

    @RequestMapping(value = REGISTRATION_REGISTER, method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<Registrationresponse> subscribes(@RequestBody Registrationrequest request) throws IOException {
        if(subscriberRepository.findByEmail(request.getEmail())!=null){
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
        User user = new User();
        user.setEmail(request.getEmail());
        user.setPassword(request.getPassword());
        user = subscriberRepository.saveAndFlush(user);

        Registrationresponse response = new Registrationresponse();
        response.setId(user.getId());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

   /* @RequestMapping(value = REGISTRATION_AUTH, method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<String> authorize(@RequestBody Subscriber subscriber) throws IOException {
        Subscriber s = subscriberRepository.findByEmailAndPassword(subscriber.getEmail(), subscriber.getPassword());
        if(s == null){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(UUID.randomUUID().toString(), HttpStatus.OK);
    }*/

    @ResponseStatus(value= HttpStatus.INTERNAL_SERVER_ERROR, reason="Error occurred!")
    @ExceptionHandler({IOException.class, NullPointerException.class})
    public void errorHandler() {
    }

}
