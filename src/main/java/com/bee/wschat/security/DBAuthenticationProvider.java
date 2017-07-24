package com.bee.wschat.security;

import com.bee.wschat.dao.ChatUserDao;
import com.bee.wschat.entity.ChatUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collection;
import java.util.Collections;

/**
 * Created by Chaklader on 7/24/17.
 */
public class DBAuthenticationProvider implements AuthenticationProvider {

    @Autowired
    private ChatUserDao chatUserDao;

    @Override
    public boolean supports(Class<?> authentication) {
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        UsernamePasswordAuthenticationToken token = (UsernamePasswordAuthenticationToken) authentication;
        String loginName = token.getName();
        String password = (String) token.getCredentials();
        ChatUser user = chatUserDao.getUserByLoginNameAndPassword(loginName, password);
        if (user == null) {
            throw new BadCredentialsException("Invalid user name or password");
        }
        Collection<? extends GrantedAuthority> authorities = Collections.singleton(new SimpleGrantedAuthority("ROLE_USER"));
        return new UsernamePasswordAuthenticationToken(loginName, password, authorities);
    }
}
