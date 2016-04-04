package com.zooplus.openexchange.service.database.repositories;

import com.zooplus.openexchange.service.database.domain.Role;
import com.zooplus.openexchange.starters.RepositoriesStarter;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(RepositoriesStarter.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@ActiveProfiles("repository")
public class TestRoleRepository {
    @Autowired
    private RoleRepository roleRepository;

    @Test
    public void testFindRoleByName() throws Exception {
        Role role = roleRepository.findByName("ADMIN");
        Assert.assertNotNull(role);
        Assert.assertNotNull(role.getId());
        Assert.assertEquals(role.getAuthority(), "ROLE_ADMIN");

        role = roleRepository.findByName("NONE");
        Assert.assertNull(role);
    }
}
