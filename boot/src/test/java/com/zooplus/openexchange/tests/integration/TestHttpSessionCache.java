package com.zooplus.openexchange.tests.integration;

import com.zooplus.openexchange.clients.integration.TestIntegrationClient;
import com.zooplus.openexchange.service.database.domain.Role;
import com.zooplus.openexchange.service.database.domain.User;
import com.zooplus.openexchange.service.database.repositories.RoleRepository;
import com.zooplus.openexchange.service.database.repositories.UserRepository;
import com.zooplus.openexchange.starters.IntegrationStarter;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Collections;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(IntegrationStarter.class)
@WebIntegrationTest("server.port:0")
@ActiveProfiles("integration")
public class TestHttpSessionCache extends TestIntegrationClient {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    public void testSessionCache() throws Exception {
        Role role = roleRepository.findByName("USER");
        Assert.assertNotNull(role);

        User user = new User("testUser", passwordEncoder.encode("testUserPassword"), "testUser@zooplus.com", Collections.singleton(role));
        user = userRepository.saveAndFlush(user);
        Assert.assertNotNull(user);
        Assert.assertTrue(userRepository.exists(user.getId()));

        // Login for the first time
        String userSessionToken = loginWithUserNameAndPassword(user.getName(), user.getPassword());
        Assert.assertNotNull(userSessionToken);

        // Login for the second time
        String userAnotherSessionToken = loginWithUserNameAndPassword(user.getName(), user.getPassword());
        Assert.assertNotNull(userAnotherSessionToken);

        // Compare tokens
        Assert.assertEquals(userSessionToken, userAnotherSessionToken);
    }
}
