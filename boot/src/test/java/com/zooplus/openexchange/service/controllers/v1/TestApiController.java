package com.zooplus.openexchange.service.controllers.v1;

import com.zooplus.openexchange.protocol.v1.Loginresponse;
import com.zooplus.openexchange.service.data.repositories.RoleRepository;
import com.zooplus.openexchange.service.data.repositories.UserRepository;
import com.zooplus.openexchange.service.security.SecurityConfig;
import com.zooplus.openexchange.service.utils.SequenceGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;

import static com.zooplus.openexchange.service.controllers.v1.ApiController.USER_AUTHENTICATE_PATH;
import static com.zooplus.openexchange.service.security.SecurityConfig.AUTH_HEADER_PASSWORD;
import static com.zooplus.openexchange.service.security.SecurityConfig.AUTH_HEADER_USERNAME;

public abstract class TestApiController {
    private static final String TEST_ENDPOINT_TEMPLATE = "http://localhost:%s";

    @Value("${local.server.port}")
    private int port;
    @Value("${admin.name}")
    protected String adminName;
    @Value("${admin.password}")
    protected String adminPassword;
    @Value("${admin.email}")
    protected String adminEmail;

    @Autowired
    protected UserRepository userRepository;
    @Autowired
    protected RoleRepository roleRepository;
    @Autowired
    protected SequenceGenerator generator;
    @Autowired
    protected PasswordEncoder passwordEncoder;

    protected HttpHeaders adminHeaders = new HttpHeaders();
    protected RestTemplate client = new RestTemplate();

    @PostConstruct
    private void initAdminHeaders() {
        preInits();
        // Add adminHeaders
        HttpHeaders headers = new HttpHeaders();
        headers.add(AUTH_HEADER_USERNAME, adminName);
        headers.add(AUTH_HEADER_PASSWORD, adminPassword);

        // Send login request
        ResponseEntity<Loginresponse> loginResp = new RestTemplate().exchange(
                provideEndPoint() + "/" + USER_AUTHENTICATE_PATH,
                HttpMethod.POST,
                new HttpEntity<>(headers),
                Loginresponse.class);

        // Add admin authentication token to admin headers
        adminHeaders.add(SecurityConfig.AUTH_HEADER_TOKEN, loginResp.getBody().getToken());
    }

    protected void preInits(){}

    protected String provideEndPoint(){
        return String.format(TEST_ENDPOINT_TEMPLATE, port);
    }
}
