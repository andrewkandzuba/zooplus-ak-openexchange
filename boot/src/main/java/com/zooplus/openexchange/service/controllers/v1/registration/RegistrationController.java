package com.zooplus.openexchange.service.controllers.v1.registration;

import com.zooplus.openexchange.protocol.v1.Registrationrequest;
import com.zooplus.openexchange.protocol.v1.Registrationresponse;
import com.zooplus.openexchange.service.data.domain.User;
import com.zooplus.openexchange.service.data.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

import static com.zooplus.openexchange.service.controllers.v1.Constants.*;

@RestController
class RegistrationController {
    static final String REGISTRATION_REGISTER = "/" + API  + "/" + VERSION_1 + "/" + REGISTRATION + "/" + REGISTER;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @RequestMapping(value = REGISTRATION_REGISTER, method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<Registrationresponse> register(@RequestBody Registrationrequest request) throws IOException {
        if(userRepository.findByEmail(request.getEmail())!=null){
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
        User user = new User();
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user = userRepository.saveAndFlush(user);

        Registrationresponse response = new Registrationresponse();
        response.setId(user.getId());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @ResponseStatus(value= HttpStatus.INTERNAL_SERVER_ERROR, reason="Error occurred!")
    @ExceptionHandler({IOException.class, NullPointerException.class})
    public void errorHandler() {
    }

}
