package com.zooplus.openexchange.controllers;

import com.zooplus.openexchange.database.domain.Role;
import com.zooplus.openexchange.database.domain.User;
import com.zooplus.openexchange.database.repositories.RoleRepository;
import com.zooplus.openexchange.database.repositories.UserRepository;
import com.zooplus.openexchange.protocol.cas.MetaInfo;
import com.zooplus.openexchange.protocol.cas.RegistrationRequest;
import com.zooplus.openexchange.protocol.cas.RegistrationResponse;
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
@RequestMapping("/")
@PreAuthorize("hasAuthority('ROLE_ADMIN')")
class UsersController  {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @RequestMapping(method = RequestMethod.PUT,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE,
            path = MetaInfo.USERS_RESOURCE)

    ResponseEntity<RegistrationResponse> create(@RequestBody RegistrationRequest request) throws IOException {
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

    @ResponseStatus(value= HttpStatus.INTERNAL_SERVER_ERROR, reason="Error occurred!")
    @ExceptionHandler({IOException.class, NullPointerException.class})
    public void errorHandler() {}
}
