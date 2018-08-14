package com.authorization.service.server.web.service;

import com.authorization.service.server.common.Role;
import com.authorization.service.server.model.User;
import com.authorization.service.server.repository.UserRepository;
import com.authorization.service.server.web.request.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AuthorizationBusinessService {

    private final UserRepository userRepository;

    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    public AuthorizationBusinessService(UserRepository userRepository, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    public ResponseEntity<User> findOne(String username) {
        return userRepository.findByUsername(username)
                .map(v -> ResponseEntity.ok(v))
                .orElse(ResponseEntity.notFound().build());
    }

    public ResponseEntity<List<User>> findAll() {
        return ResponseEntity.ok(userRepository.findAll());
    }

    public ResponseEntity<User> post(UserCreateRequest request) {
        Optional<User> optionalUser = userRepository.findByUsername(request.getUsername());
        if (optionalUser.isPresent()) {
            return ResponseEntity.status(417).build();
        }

        User user = User.builder()
                .username(request.getUsername())
                .password(bCryptPasswordEncoder.encode(request.getPassword()))
                .roles(new ArrayList<Role>(){{add(Role.USER);}})
                .build();

        return ResponseEntity.status(201).body(userRepository.save(user));
    }

    public ResponseEntity<User> updatePassword(UserUpdatePasswordRequest request) {
        String encode = bCryptPasswordEncoder.encode(request.getOldPassword());
        Optional<User> optionalUser = userRepository.findByUsername(request.getUsername());
        if (optionalUser.isPresent() && optionalUser.get().getPassword().equals(encode)) {
            User next = optionalUser.get();
            next.setPassword(encode);
            return ResponseEntity.ok(userRepository.save(next));
        }
        return ResponseEntity.status(417).build();
    }

    public ResponseEntity<User> addRole(UserAddRoleRequest request) {
        Optional<User> optionalUser = userRepository.findByUsername(request.getUsername());
        if (optionalUser.isPresent()) {
            User original = optionalUser.get();
            original.getRoles()
                    .add(request.getRole());
            return ResponseEntity.ok(userRepository.save(original));
        }
        return ResponseEntity.status(417).build();
    }

    public ResponseEntity<User> removeRole(UserRemoveRoleRequest request) {
        Optional<User> optionalUser = userRepository.findByUsername(request.getUsername());
        if (optionalUser.isPresent()) {
            User original = optionalUser.get();
            List<Role> roles = original
                    .getRoles()
                    .stream()
                    .filter(v -> v != request.getRole())
                    .collect(Collectors.toList());
            original.setRoles(roles);
            return ResponseEntity.ok(userRepository.save(original));
        }
        return ResponseEntity.status(417).build();
    }

    public ResponseEntity<User> remove(String username) {
        Optional<User> optionalUser = userRepository.findByUsername(username);
        if (optionalUser.isPresent()) {
            User original = optionalUser.get();
            userRepository.delete(original);
            return ResponseEntity.ok(original);
        }
        return ResponseEntity.status(417).build();
    }
}
