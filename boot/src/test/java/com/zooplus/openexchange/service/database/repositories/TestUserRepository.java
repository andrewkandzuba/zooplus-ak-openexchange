package com.zooplus.openexchange.service.database.repositories;

import com.zooplus.openexchange.service.database.domain.Role;
import com.zooplus.openexchange.service.database.domain.User;
import com.zooplus.openexchange.starters.RepositoriesStarter;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(RepositoriesStarter.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@ActiveProfiles("test")
public class TestUserRepository {
    @Autowired
    private ExecutorService executorService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Value("${admin.name}")
    private String adminName;
    @Value("${admin.password}")
    private String adminPassword;
    @Value("${admin.email}")
    private String adminEmail;

    @Test
    public void testAddNewUser() throws Exception {
        Assert.assertNotNull(userRepository);
        List<User> users =  userRepository.findAll();
        Assert.assertEquals(users.size(), 1);
        User user = new User("user1", passwordEncoder.encode("someuserpassword"), "user1@zooplus.com");
        User saved = userRepository.saveAndFlush(user);

        Assert.assertNotNull(saved);
        Assert.assertNotNull(saved.getId());
        Assert.assertNotNull(saved.getCreatedAt());
        Assert.assertTrue(saved.getEnabled());
        Assert.assertEquals(saved.getPassword(), user.getPassword());
        Assert.assertEquals(userRepository.findAll().size(), 2);
    }

    @Test
    public void testContextCleanUp() throws Exception {
        List<User> users =  userRepository.findAll();
        Assert.assertEquals(users.size(), 1);
    }

    @Test
    public void testTransactions() throws Exception {
        CountDownLatch beforeCommit = new CountDownLatch(1);
        CountDownLatch afterCommit = new CountDownLatch(1);
        Assert.assertEquals(userRepository.findAll().size(), 1);
        executorService.submit(() -> {
            try {
                beforeCommit.await(5000L, TimeUnit.MILLISECONDS);
                Assert.assertEquals(userRepository.findAll().size(), 1);
                beforeCommit.await(5000L, TimeUnit.MILLISECONDS);
                Assert.assertEquals(userRepository.findAll().size(), 2);
            } catch (InterruptedException e) {
                e.printStackTrace();
                Assert.assertTrue(false);
            }
        });
        User user = new User("user1", passwordEncoder.encode("someuserpassword"), "user1@zooplus.com");
        beforeCommit.countDown();
        User savedUser = userRepository.save(user);
        afterCommit.countDown();
        Assert.assertNotNull(savedUser);
        Assert.assertNotNull(savedUser.getId());
        Assert.assertTrue(savedUser.getEnabled());
        Assert.assertNotNull(savedUser.getCreatedAt());
        Assert.assertNotNull(savedUser.getId());
    }

    @Test
    public void testFindUserByEmail() throws Exception {
        User user = userRepository.findByName(adminName);
        Assert.assertNotNull(user);
        Assert.assertNotNull(user.getId());
        Assert.assertEquals(user.getEmail(), adminEmail);
        Assert.assertEquals(user.getPassword(), adminPassword);
        Assert.assertTrue(user.getEnabled());
        Assert.assertNotNull(user.getCreatedAt());
        Assert.assertNotNull(user.getRoles());
        Assert.assertTrue(user.getRoles().stream().anyMatch(role -> role.getName().equalsIgnoreCase("ADMIN")));
        Assert.assertTrue(user.getRoles().stream().anyMatch(role -> role.getName().equalsIgnoreCase("USER")));

        Assert.assertNull(userRepository.findByName("none@zooplus.com"));
        user = new User("none", passwordEncoder.encode("someuserpassword"), "none@zooplus.com");
        user.setRoles(Collections.singleton(new Role(2L, "USER")));
        user = userRepository.saveAndFlush(user);
        User savedUser = userRepository.findOne(user.getId());
        Assert.assertNotNull(savedUser.getCreatedAt());
        Assert.assertTrue(savedUser.getEnabled());
        Assert.assertTrue(savedUser.getRoles().stream().anyMatch(r -> r.getName().equalsIgnoreCase("USER")));
        Assert.assertFalse(user.getRoles().stream().anyMatch(r -> r.getName().equalsIgnoreCase("ADMIN")));
    }
}
