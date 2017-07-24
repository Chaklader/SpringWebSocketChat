package com.bee.wschat.security;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;

import java.util.List;

/**
 * Created by Chaklader on 7/24/17.
 */
public class SimpleAuthenticationProvider implements AuthenticationProvider {

    private static final String SECURE_ADMIN_PASSWORD = "rockandroll";

    @Override
    public boolean supports(Class<?> authentication) {
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        UsernamePasswordAuthenticationToken token = (UsernamePasswordAuthenticationToken) authentication;

        List<GrantedAuthority> authorities = SECURE_ADMIN_PASSWORD.equals(token.getCredentials()) ?
                AuthorityUtils.createAuthorityList("ROLE_ADMIN") : null;

        return new UsernamePasswordAuthenticationToken(token.getName(), token.getCredentials(), authorities);
    }
}
