package com.zooplus.openexchange.service.data.repositories;

import com.zooplus.openexchange.service.data.domain.Role;
import com.zooplus.openexchange.service.data.domain.User;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
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
@ActiveProfiles("development")
public class TestUserRepository {
    @Autowired
    private ExecutorService executorService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    public void testAddNewUser() throws Exception {
        Assert.assertNotNull(userRepository);
        List<User> users =  userRepository.findAll();
        Assert.assertEquals(users.size(), 1);

        User user = new User();
        user.setEmail("user1@zooplus.com");
        user.setPassword(passwordEncoder.encode("someuserpassword"));
        User saved = userRepository.saveAndFlush(user);
        Assert.assertNotNull(saved);
        Assert.assertNotNull(saved.getId());
        Assert.assertNotNull(saved.getCreatedAt());
        Assert.assertTrue(saved.getEnabled());
        Assert.assertTrue(passwordEncoder.matches("someuserpassword", saved.getPassword()));
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

        User user = new User();
        user.setEmail("user1@zooplus.com");
        user.setPassword("someuserpassword");
        beforeCommit.countDown();
        User saved = userRepository.save(user);
        afterCommit.countDown();
        Assert.assertNotNull(saved);
        Assert.assertNotNull(saved.getId());
        Assert.assertTrue(saved.getEnabled());
        Assert.assertNotNull(saved.getCreatedAt());
        Assert.assertNotNull(saved.getId());
    }

    @Test
    public void testFindUserByEmail() throws Exception {
        final String adminUserName = "admin@zooplus.com";
        final String adminPassword = "pwd12345";

        User user = userRepository.findByEmail(adminUserName);
        Assert.assertNotNull(user);
        Assert.assertNotNull(user.getId());
        Assert.assertEquals(user.getEmail(), adminUserName);
        Assert.assertEquals(user.getPassword(), adminPassword);
        Assert.assertTrue(user.getEnabled());
        Assert.assertNotNull(user.getCreatedAt());
        Assert.assertNotNull(user.getRoles());
        Assert.assertTrue(user.getRoles().stream().anyMatch(role -> role.getName().equalsIgnoreCase("ADMIN")));
        Assert.assertTrue(user.getRoles().stream().anyMatch(role -> role.getName().equalsIgnoreCase("USER")));

        Assert.assertNull(userRepository.findByEmail("none@zooplus.com"));
        user = new User();
        user.setEmail("none@zooplus.com");
        user.setPassword("querty");
        user.setRoles(Collections.singleton(new Role(2L, "USER")));
        user = userRepository.saveAndFlush(user);
        User savedUser = userRepository.findOne(user.getId());
        Assert.assertNotNull(savedUser.getCreatedAt());
        Assert.assertTrue(savedUser.getEnabled());
        Assert.assertTrue(savedUser.getRoles().stream().anyMatch(r -> r.getName().equalsIgnoreCase("USER")));
        Assert.assertFalse(user.getRoles().stream().anyMatch(r -> r.getName().equalsIgnoreCase("ADMIN")));
    }
}
