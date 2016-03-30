package com.zooplus.openexchange.service.data.cache;


import com.zooplus.openexchange.service.utils.ApplicationUtils;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
public class TokenServiceImpl implements TokenService {
    @Override
    public Authentication issue(String userName, Collection<? extends GrantedAuthority> authorities) {
        UsernamePasswordAuthenticationToken userAuth = new UsernamePasswordAuthenticationToken(userName, null, authorities);
        userAuth.setDetails(ApplicationUtils.nextToken());
        return userAuth;
    }
}
