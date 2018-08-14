package com.authorization.service.server.security.model;

import com.authorization.service.server.model.Oauth2User;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public class UserDetails extends org.springframework.security.core.userdetails.User {

    @Getter
    private Oauth2User oauth2User;

    public UserDetails(Oauth2User oauth2User, String username, String password, Collection<? extends GrantedAuthority> authorities) {
        super(username, password, authorities);
        this.oauth2User = oauth2User;
    }
}
