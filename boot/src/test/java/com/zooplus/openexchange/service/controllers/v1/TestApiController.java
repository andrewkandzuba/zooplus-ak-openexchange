package com.zooplus.openexchange.service.controllers.v1;

import com.zooplus.openexchange.service.data.domain.Role;
import com.zooplus.openexchange.service.data.domain.User;
import com.zooplus.openexchange.service.data.repositories.RoleRepository;
import com.zooplus.openexchange.service.data.repositories.UserRepository;
import com.zooplus.openexchange.service.utils.SequenceGenerator;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import java.util.Base64;
import java.util.Collections;

public abstract class TestApiController {
    private static final String TEST_ENDPOINT_TEMPLATE = "http://localhost:%s";
    @Value("${local.server.port}")
    private int port;
    @Value("${admin.name}")
    private String adminName;
    @Value("${admin.password}")
    private String adminPassword;
    @Value("${admin.email}")
    private String adminEmail;

    @Autowired
    protected UserRepository userRepository;
    @Autowired
    protected RoleRepository roleRepository;
    @Autowired
    protected SequenceGenerator generator;
    @Autowired
    protected PasswordEncoder passwordEncoder;

    protected final RestTemplate restTemplate = new RestTemplate();
    protected final HttpHeaders headers = new HttpHeaders();

    @PostConstruct
    protected void initHeaders() {
        // Add headers
        headers.add("Authorization", "Basic " +
                Base64.getEncoder().encodeToString((adminName + ":" + adminPassword).getBytes()));
    }

    protected final void mockAdminAccess(UserRepository userRepository, SequenceGenerator generator){
        // Mock data and repository behaviour
        User admin = new User(adminName, adminPassword, adminEmail);
        admin.setRoles(Collections.singleton(new Role(generator.nextLong(), "ADMIN")));
        MockitoAnnotations.initMocks(this);
        Mockito.when(userRepository.findByNameAndPassword(adminName, adminPassword)).thenReturn(admin);
    }

    protected String provideEndPoint(){
        return String.format(TEST_ENDPOINT_TEMPLATE, port);
    }
}
