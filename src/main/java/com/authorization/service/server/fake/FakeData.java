package com.authorization.service.server.fake;

import com.authorization.service.server.common.Role;
import com.authorization.service.server.model.User;
import com.authorization.service.server.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.time.LocalDate;
import java.util.ArrayList;

@Service
public class FakeData {

    private final UserRepository userRepository;

    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    public FakeData(UserRepository userRepository, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @PostConstruct
    public void fakeData() {
        userRepository.deleteAll();

        for (int i = 0; i < 10; i++) {
            User user = new User();
            user.setUsername("user" + i);
            user.setPassword(bCryptPasswordEncoder.encode("user" + i));
            user.setRoles(new ArrayList<Role>() {{
                add(Role.USER);
            }});
            user.setPost(LocalDate.now());
            user.setActive(true);
            userRepository.save(user);
        }

        for (int i = 0; i < 10; i++) {
            User user = new User();
            user.setUsername("admin" + i);
            user.setPassword(bCryptPasswordEncoder.encode("admin" + i));
            user.setRoles(new ArrayList<Role>() {{
                add(Role.USER);
                add(Role.ADMIN);
            }});
            user.setPost(LocalDate.now());
            user.setActive(true);
            userRepository.save(user);
        }
    }
}
