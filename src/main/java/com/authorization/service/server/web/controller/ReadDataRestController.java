package com.authorization.service.server.web.controller;

import com.authorization.service.server.model.User;
import com.authorization.service.server.web.service.AuthorizationBusinessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/read/user")
public class ReadDataRestController {

    private final AuthorizationBusinessService authorizationBusinessService;

    @Autowired
    public ReadDataRestController(AuthorizationBusinessService authorizationBusinessService) {
        this.authorizationBusinessService = authorizationBusinessService;
    }

    @PreAuthorize("authentication.name == #username || hasRole('ROLE_ADMIN')")
    @GetMapping("/username")
    public ResponseEntity<User> findOne(@RequestParam(value = "username") String username) {
        return authorizationBusinessService.findOne(username);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping
    public ResponseEntity<List<User>> findAll() {
        return authorizationBusinessService.findAll();
    }
}
