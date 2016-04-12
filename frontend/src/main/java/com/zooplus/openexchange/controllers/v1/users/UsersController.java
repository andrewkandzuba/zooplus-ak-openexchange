package com.zooplus.openexchange.controllers.v1.users;

import com.zooplus.openexchange.controllers.v1.Version;
import com.zooplus.openexchange.database.domain.Role;
import com.zooplus.openexchange.database.domain.User;
import com.zooplus.openexchange.database.repositories.RoleRepository;
import com.zooplus.openexchange.database.repositories.UserRepository;
import com.zooplus.openexchange.protocol.rest.v1.*;
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

import static com.zooplus.openexchange.controllers.v1.Version.USERS_ENDPOINT;

@RestController
@RequestMapping(USERS_ENDPOINT)
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
    ResponseEntity<RegistrationResponse> register(@RequestBody RegistrationRequest request) throws IOException {
        if(userRepository.findByName(request.getName())!=null){
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
        Role role = roleRepository.findByName("USER");
        User user = new User(request.getName(), passwordEncoder.encode(request.getPassword()), request.getEmail());
        user.setRoles(Collections.singleton(role));
        User addedUser = userRepository.saveAndFlush(user);
        RegistrationResponse response = new RegistrationResponse();
        response.setId(addedUser.getId());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.GET, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE, path = USER_SESSION_DETAILS_PATH)
    @PreAuthorize("hasAuthority('ROLE_USER')")
    ResponseEntity<SessionDetailsResponse> sessionDetails(HttpSession session) throws IOException {
        SessionDetailsResponse response = new SessionDetailsResponse();
        response.setSessionId(session.getId());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE, path = USER_LOGOUT_PATH)
    @PreAuthorize("hasAuthority('ROLE_USER')")
    ResponseEntity<LogoutResponse> logout(HttpSession session) throws IOException {
        long sessionLifeTime = System.currentTimeMillis() - session.getCreationTime();
        session.removeAttribute("_csrf");
        session.invalidate();
        LogoutResponse logoutresponse = new LogoutResponse();
        logoutresponse.setSessionLifeTime(sessionLifeTime);
        return new ResponseEntity<>(logoutresponse, HttpStatus.OK);
    }

    @ResponseStatus(value= HttpStatus.INTERNAL_SERVER_ERROR, reason="Error occurred!")
    @ExceptionHandler({IOException.class, NullPointerException.class})
    public void errorHandler() {}
}
