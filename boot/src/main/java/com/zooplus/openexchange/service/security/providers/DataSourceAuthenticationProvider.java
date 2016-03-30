package com.zooplus.openexchange.service.security.providers;

import com.zooplus.openexchange.service.data.cache.AuthenticationService;
import com.zooplus.openexchange.service.data.cache.TokenService;
import com.zooplus.openexchange.service.data.domain.User;
import com.zooplus.openexchange.service.data.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class DataSourceAuthenticationProvider implements AuthenticationProvider {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private AuthenticationService authenticationService;
    @Autowired
    private TokenService tokenService;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        Optional<String> username = (Optional) authentication.getPrincipal();
        Optional<String> password = (Optional) authentication.getCredentials();
        if(!username.isPresent() && !password.isPresent()){
            throw new BadCredentialsException("Empty credentials");
        }
        User user = userRepository.findByNameAndPassword(username.get(), password.get());
        if(user == null){
            throw new BadCredentialsException("Invalid credentials");
        }
        Authentication userAuth = tokenService.issue(user.getName(), user.getRoles());
        authenticationService.cache(userAuth);
        return userAuth;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return aClass.equals(UsernamePasswordAuthenticationToken.class);
    }
}
