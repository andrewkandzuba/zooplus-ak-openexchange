package com.zooplus.openexchange.service.controllers.v1;


import com.zooplus.openexchange.service.data.cache.TokenService;
import com.zooplus.openexchange.service.data.domain.Role;
import com.zooplus.openexchange.service.data.domain.User;
import com.zooplus.openexchange.service.data.cache.AuthenticationService;
import com.zooplus.openexchange.service.data.repositories.RoleRepository;
import com.zooplus.openexchange.service.data.repositories.UserRepository;
import com.zooplus.openexchange.service.utils.ApplicationUtils;
import com.zooplus.openexchange.service.utils.SequenceGenerator;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Collections;

public class TestApiMockRepositoriesController extends TestApiController {
    @Autowired
    protected UserRepository userRepository;
    @Autowired
    protected RoleRepository roleRepository;
    @Autowired
    protected AuthenticationService authenticationService;
    @Autowired
    protected TokenService tokenService;
    @Autowired
    protected SequenceGenerator generator;
    @Autowired
    protected PasswordEncoder passwordEncoder;

    @Override
    protected void preInits() {
        // Mock admin user
        User admin = new User(adminName, adminPassword, adminEmail);
        admin.setRoles(Collections.singleton(new Role(generator.nextLong(), "ADMIN")));
        MockitoAnnotations.initMocks(this);
        Mockito.when(userRepository.findByNameAndPassword(adminName, adminPassword)).thenReturn(admin);

        // Mock token issuing
        UsernamePasswordAuthenticationToken adminAuthenticationToken = new UsernamePasswordAuthenticationToken(admin.getName(), null, admin.getRoles());
        adminAuthenticationToken.setDetails(ApplicationUtils.nextToken());
        Mockito.when(tokenService.issue(admin.getName(), admin.getRoles())).thenReturn(adminAuthenticationToken);

        // Mock cache data
        Mockito.when(authenticationService.get(adminAuthenticationToken.getDetails().toString())).thenReturn(adminAuthenticationToken);
    }
}
