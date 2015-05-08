package com.autodesk.icp.community.authentication;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import com.autodesk.icp.community.common.model.User;
import com.autodesk.icp.community.common.model.UserPrincipal;
import com.autodesk.icp.community.service.AuthenticationService;

public class CustomAuthenticationProvider implements AuthenticationProvider {

    @Autowired
    private AuthenticationService authService;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        if (!supports(authentication.getClass())) {
            return null;
        }

        String name = authentication.getPrincipal().toString();
        String password = authentication.getCredentials().toString();

        User authedUser = new User();//authService.login(name, password);
        authedUser.setLoginId(name);
        authedUser.setDisplayName(name);   
        authedUser.setName("dummyUser");
        if (password.isEmpty()) {
            authedUser.setAuthed(false);
        } else {
            authedUser.setAuthed(true);
        }
        if (authedUser.isAuthed()) {

            List<GrantedAuthority> authorities = new ArrayList<>();
            authorities.add(new SimpleGrantedAuthority("USER_ROLE"));
            UsernamePasswordAuthenticationToken authenticated = new UsernamePasswordAuthenticationToken(new UserPrincipal(authedUser),
                                                                                                        password,
                                                                                                        authorities);

            return authenticated;
        } else {
            throw new BadCredentialsException("Authentication failed.");
        }

    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
}