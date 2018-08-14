package com.authorization.service.server.security.service;

import com.authorization.service.server.model.Oauth2User;
import com.authorization.service.server.repository.Oauth2UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final Oauth2UserRepository oauth2UserRepository;

    @Autowired
    public UserDetailsServiceImpl(Oauth2UserRepository oauth2UserRepository) {
        this.oauth2UserRepository = oauth2UserRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Oauth2User oauth2User = oauth2UserRepository.findByUsername(username);
        if (oauth2User == null) {
            throw new UsernameNotFoundException("user by username " + username + " not found!");
        }

        List<SimpleGrantedAuthority> authorities = oauth2User
                .getRoles()
                .stream().map(v -> new SimpleGrantedAuthority("ROLE_" + v))
                .collect(Collectors.toList());

        return new com.authorization.service.server.security.model.UserDetails(oauth2User, oauth2User.getUsername(), oauth2User.getPassword(), authorities);
    }
}
