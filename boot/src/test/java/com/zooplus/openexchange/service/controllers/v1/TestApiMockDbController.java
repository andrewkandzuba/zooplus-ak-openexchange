package com.zooplus.openexchange.service.controllers.v1;


import com.zooplus.openexchange.service.data.domain.Role;
import com.zooplus.openexchange.service.data.domain.User;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.Collections;

public class TestApiMockDbController extends TestApiController {
    @Override
    protected void preInits() {
        // Mock admin user
        User admin = new User(adminName, adminPassword, adminEmail);
        admin.setRoles(Collections.singleton(new Role(generator.nextLong(), "ADMIN")));
        MockitoAnnotations.initMocks(this);
        Mockito.when(userRepository.findByNameAndPassword(adminName, adminPassword)).thenReturn(admin);
    }
}
