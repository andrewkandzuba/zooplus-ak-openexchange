package com.zooplus.openexchange.tests.unit;

import com.google.common.collect.Sets;
import com.zooplus.openexchange.service.frontend.database.domain.Role;
import com.zooplus.openexchange.service.frontend.database.domain.User;
import com.zooplus.openexchange.service.frontend.database.repositories.RoleRepository;
import com.zooplus.openexchange.service.frontend.database.repositories.UserRepository;
import com.zooplus.openexchange.tests.integration.TestLocalRestClient;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.BoundSetOperations;
import org.springframework.data.redis.core.BoundValueOperations;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.session.MapSession;

import javax.annotation.PostConstruct;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

public class TestMockedClient extends TestLocalRestClient {
    private static final String PRINCIPAL_NAME_INDEX_NAME = "org.springframework.session.FindByIndexNameSessionRepository.PRINCIPAL_NAME_INDEX_NAME";
    private static final String SPRING_SECURITY_CONTEXT = "SPRING_SECURITY_CONTEXT";
    private static final String SESSION_ATTRIBUTE_PREFIX = "sessionAttr:";

    @Value("${admin.name}")
    private String adminName;
    @Value("${admin.password}")
    private String adminPassword;
    @Value("${admin.email}")
    private String adminEmail;
    @Value("${admin.session.token}")
    private String adminSessionToken;

    @Autowired
    private RedisOperations<Object, Object> redisOperations;
    @Autowired
    private BoundHashOperations<Object, Object, Object> boundHashOperations;
    @Autowired
    private BoundSetOperations<Object, Object> boundSetOperations;
    @Autowired
    private BoundValueOperations<Object, Object> boundValueOperations;

    private volatile long nextId = 0;
    @Autowired
    protected UserRepository userRepository;
    @Autowired
    protected RoleRepository roleRepository;
    @Autowired
    protected PasswordEncoder passwordEncoder;

    public String getAdminSessionToken() {
        return adminSessionToken;
    }

    public String getAdminName() {
        return adminName;
    }

    protected long getNextId(){
        return ++nextId;
    };

    protected User mockUser(String userName, String userPassword, String userEmail, Set<Role> roles){
        User user = new User(userName, passwordEncoder.encode(userPassword), userEmail, roles);
        user.setId(getNextId());
        Mockito.when(userRepository.findByName(user.getName())).thenReturn(user);
        Mockito.when(userRepository.findByNameAndPassword(user.getName(), user.getPassword())).thenReturn(user);
        Mockito.when(userRepository.findOne(user.getId())).thenReturn(user);
        return user;
    }

    protected void mockUserRedisSession(User user, String sessionToken) {
        // Mock user authentication
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(user.getName(), null, user.getRoles());

        // Mock admin cached token
        MapSession adminSession = new MapSession(sessionToken);
        adminSession.setCreationTime(System.currentTimeMillis());
        adminSession.setMaxInactiveIntervalInSeconds(100);
        adminSession.setLastAccessedTime(System.currentTimeMillis());
        adminSession.setAttribute(PRINCIPAL_NAME_INDEX_NAME, getAdminName());

        // Emulate security content
        SecurityContext securityContext = new SecurityContextImpl();
        securityContext.setAuthentication(usernamePasswordAuthenticationToken);
        adminSession.setAttribute(SPRING_SECURITY_CONTEXT, securityContext);

        // Mock redisOperations
        Mockito.when(redisOperations.boundHashOps(Mockito.anyString())).thenReturn(boundHashOperations);
        Mockito.when(boundHashOperations.entries()).thenReturn(toMap(adminSession));
        Mockito.doNothing().when(boundHashOperations).putAll(Mockito.anyMap());
        Mockito.when(redisOperations.boundSetOps(Mockito.any())).thenReturn(boundSetOperations);
        Mockito.when(boundSetOperations.add(Mockito.any(Object[].class))).thenReturn(0L);
        Mockito.when(boundSetOperations.members()).thenReturn(Collections.emptySet());

        // Mock expiration
        Mockito.when(redisOperations.boundValueOps(Mockito.anyString())).thenReturn(boundValueOperations);
        Mockito.when(boundValueOperations.append(Mockito.anyString())).thenReturn(0);
        Mockito.when(boundValueOperations.expire(Mockito.anyLong(), Mockito.any(TimeUnit.class))).thenReturn(false);
    }

    @PostConstruct
    private void initMocks(){
        Set<Role> allRoles = mockRoles();
        User adminUser = mockUser(adminName, adminPassword, adminEmail, allRoles);
        mockUserRedisSession(adminUser, adminSessionToken);
    }

    private Set<Role> mockRoles(){
        Role adminRole = new Role(getNextId(), "ADMIN");
        Role userRole = new Role(getNextId(), "USER");
        Mockito.when(roleRepository.findByName("ADMIN")).thenReturn(adminRole);
        Mockito.when(roleRepository.findByName("USER")).thenReturn(userRole);
        return Sets.newHashSet(adminRole, userRole);
    }

    private Map<Object, Object> toMap(MapSession session) {
        Map<Object, Object> result = new HashMap<>();
        result.put("creationTime", session.getCreationTime());
        result.put("maxInactiveInterval", session.getMaxInactiveIntervalInSeconds());
        result.put("lastAccessedTime", session.getCreationTime());
        result.put(SESSION_ATTRIBUTE_PREFIX + SPRING_SECURITY_CONTEXT, session.getAttribute(SPRING_SECURITY_CONTEXT));
        result.put(SESSION_ATTRIBUTE_PREFIX + PRINCIPAL_NAME_INDEX_NAME,
                session.getAttribute(PRINCIPAL_NAME_INDEX_NAME));
        return result;
    }
}
