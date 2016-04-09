package com.zooplus.openexchange.controllers.v1.users;

import com.zooplus.openexchange.controllers.v1.Version;
import com.zooplus.openexchange.database.domain.Role;
import com.zooplus.openexchange.database.domain.User;
import com.zooplus.openexchange.database.repositories.RoleRepository;
import com.zooplus.openexchange.database.repositories.UserRepository;
import com.zooplus.openexchange.protocol.rest.v1.Logoutresponse;
import com.zooplus.openexchange.protocol.rest.v1.Registrationrequest;
import com.zooplus.openexchange.protocol.rest.v1.Registrationresponse;
import com.zooplus.openexchange.protocol.rest.v1.Sessiondetailsresponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Collections;

@RestController
class UsersController extends Version {
    private final static Logger logger = LoggerFactory.getLogger(UsersController.class);

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @RequestMapping(method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE, path = USER_REGISTRATION_PATH)
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    ResponseEntity<Registrationresponse> register(@RequestBody Registrationrequest request) throws IOException {
        if(userRepository.findByName(request.getName())!=null){
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
        Role role = roleRepository.findByName("USER");
        User user = new User(request.getName(), passwordEncoder.encode(request.getPassword()), request.getEmail());
        user.setRoles(Collections.singleton(role));
        User addedUser = userRepository.saveAndFlush(user);
        Registrationresponse response = new Registrationresponse();
        response.setId(addedUser.getId());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.GET, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE, path = USER_SESSION_DETAILS_PATH)
    @PreAuthorize("hasAuthority('ROLE_USER')")
    ResponseEntity<Sessiondetailsresponse> sessionDetails(HttpSession session) throws IOException {
        Sessiondetailsresponse response = new Sessiondetailsresponse();
        response.setSessionId(session.getId());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE, path = USER_LOGOUT_PATH)
    @PreAuthorize("hasAuthority('ROLE_USER')")
    ResponseEntity<Logoutresponse> logout(HttpSession session) throws IOException {
        long sessionLifeTime = System.currentTimeMillis() - session.getCreationTime();
        session.removeAttribute("_csrf");
        session.invalidate();
        Logoutresponse logoutresponse = new Logoutresponse();
        logoutresponse.setSessionLifeTime(sessionLifeTime);
        return new ResponseEntity<>(logoutresponse, HttpStatus.OK);
    }

    @ResponseStatus(value= HttpStatus.INTERNAL_SERVER_ERROR, reason="Error occurred!")
    @ExceptionHandler({IOException.class, NullPointerException.class})
    public void errorHandler() {}
}
