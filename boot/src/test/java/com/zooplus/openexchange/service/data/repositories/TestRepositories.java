package com.zooplus.openexchange.service.data.repositories;

import com.zooplus.openexchange.service.data.domain.User;
import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(RepositoriesStarter.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@ActiveProfiles("development")
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TestRepositories {

    @Autowired
    ExecutorService executorService;

    @Autowired
    UserRepository userRepository;

    @Test
    public void testA_DataSourceAccess() throws Exception {
        Assert.assertNotNull(userRepository);
        List<User> users =  userRepository.findAll();
        Assert.assertEquals(users.size(), 1);

        User user = new User();
        user.setEmail("user1@zooplus.com");
        user.setPassword("password1234");
        User saved = userRepository.save(user);
        Assert.assertNotNull(saved);
        Assert.assertNotNull(saved.getId());
        Assert.assertEquals(userRepository.findAll().size(), 2);
    }

    @Test
    public void testB_ContextCleanUp() throws Exception {
        List<User> users =  userRepository.findAll();
        Assert.assertEquals(users.size(), 1);
    }

    @Test
    public void testC_Transactions() throws Exception {
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
        user.setPassword("password1234");
        beforeCommit.countDown();
        User saved = userRepository.save(user);
        afterCommit.countDown();
        Assert.assertNotNull(saved);
        Assert.assertNotNull(saved.getId());
    }

    @Test
    public void testD_findSubscriberByEmail() throws Exception {
        Assert.assertNotNull(userRepository.findByEmail("admin@zooplus.com"));
        Assert.assertNull(userRepository.findByEmail("none@zooplus.com"));
    }
}
