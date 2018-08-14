package com.authorization.service.server.security.service;

import com.authorization.service.server.model.User;
import com.authorization.service.server.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    @Autowired
    public UserDetailsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> userOptional = userRepository.findByUsername(username);
        if (!userOptional.isPresent()) {
            throw new UsernameNotFoundException("user by username " + username + " not found!");
        }

        User user = userOptional.get();

        List<SimpleGrantedAuthority> authorities = user
                .getRoles()
                .stream().map(v -> new SimpleGrantedAuthority("ROLE_" + v))
                .collect(Collectors.toList());

        return new com.authorization.service.server.security.model.UserDetails(user, user.getUsername(), user.getPassword(), authorities);
    }
}
