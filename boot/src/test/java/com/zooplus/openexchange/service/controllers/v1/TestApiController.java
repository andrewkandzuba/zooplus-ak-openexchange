package com.zooplus.openexchange.service.controllers.v1;

import com.zooplus.openexchange.service.data.domain.Role;
import com.zooplus.openexchange.service.data.domain.User;
import com.zooplus.openexchange.service.data.repositories.UserRepository;
import com.zooplus.openexchange.service.utils.SequenceGenerator;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpHeaders;
import org.springframework.web.client.RestTemplate;

import java.util.Base64;
import java.util.Collections;

public abstract class TestApiController {
    protected final RestTemplate restTemplate = new RestTemplate();
    protected final HttpHeaders headers = new HttpHeaders();

    protected final void mockAdminAccess(UserRepository userRepository, SequenceGenerator generator){
        final String email = "admin@zooplus.com";
        final String password = "somepassword";
        final long nextUserId = generator.nextLong();
        final long nextRoleId = generator.nextLong();

        // Mock data and repository
        User admin = new User();
        admin.setId(nextUserId);
        admin.setEmail(email);
        admin.setPassword(password);
        admin.setRoles(Collections.singleton(new Role(nextRoleId, "ADMIN")));
        MockitoAnnotations.initMocks(this);
        Mockito.when(userRepository.findByEmailAndPassword(email, password)).thenReturn(admin);

        // Add headers
        headers.add("Authorization", "Basic " +
                Base64.getEncoder().encodeToString((email + ":" + password).getBytes()));
    }
}
