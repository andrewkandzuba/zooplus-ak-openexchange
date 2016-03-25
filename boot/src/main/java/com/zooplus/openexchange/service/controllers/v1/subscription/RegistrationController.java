package com.zooplus.openexchange.service.controllers.v1.subscription;

import com.zooplus.openexchange.protocol.v1.Registrationrequest;
import com.zooplus.openexchange.protocol.v1.Registrationresponse;
import com.zooplus.openexchange.service.data.domain.User;
import com.zooplus.openexchange.service.data.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

import static com.zooplus.openexchange.service.controllers.v1.Constants.*;

@RestController
class RegistrationController {
    static final String REGISTRATION_REGISTER = "/" + API  + "/" + VERSION_1 + "/" + REGISTRATION + "/" + REGISTER;
    static final String REGISTRATION_AUTH = "/" + API  + "/" + VERSION_1 + "/" + REGISTRATION + "/" + AUTH;;

    @Autowired
    private UserRepository userRepository;

    @RequestMapping(value = REGISTRATION_REGISTER, method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<Registrationresponse> subscribes(@RequestBody Registrationrequest request) throws IOException {
        if(userRepository.findByEmail(request.getEmail())!=null){
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
        User user = new User();
        user.setEmail(request.getEmail());
        user.setPassword(request.getPassword());
        user = userRepository.saveAndFlush(user);

        Registrationresponse response = new Registrationresponse();
        response.setId(user.getId());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

   /* @RequestMapping(value = REGISTRATION_AUTH, method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<String> authorize(@RequestBody Subscriber subscriber) throws IOException {
        Subscriber s = userRepository.findByEmailAndPassword(subscriber.getEmail(), subscriber.getPassword());
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
