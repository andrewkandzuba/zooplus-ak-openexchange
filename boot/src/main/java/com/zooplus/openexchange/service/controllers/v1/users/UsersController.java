package com.zooplus.openexchange.service.controllers.v1.users;

import com.zooplus.openexchange.protocol.v1.Registrationrequest;
import com.zooplus.openexchange.protocol.v1.Registrationresponse;
import com.zooplus.openexchange.service.controllers.v1.ApiController;
import com.zooplus.openexchange.service.database.domain.Role;
import com.zooplus.openexchange.service.database.domain.User;
import com.zooplus.openexchange.service.database.repositories.RoleRepository;
import com.zooplus.openexchange.service.database.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Collections;

@RestController
class UsersController extends ApiController {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @RequestMapping(method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, path = USER_REGISTRATION_PATH)
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

    @RequestMapping(method = RequestMethod.GET, path = USER_HELLO_PATH)
    @PreAuthorize("hasAuthority('ROLE_USER')")
    ResponseEntity<String> hello() throws IOException {
        return new ResponseEntity<>("hello!!!", HttpStatus.OK);
    }
}
