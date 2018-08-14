package com.authorization.service.server.fake;

import com.authorization.service.server.common.Roles;
import com.authorization.service.server.model.Oauth2User;
import com.authorization.service.server.repository.Oauth2UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.ArrayList;

@Service
public class FakeData {

    private final Oauth2UserRepository oauth2UserRepository;

    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    public FakeData(Oauth2UserRepository oauth2UserRepository, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.oauth2UserRepository = oauth2UserRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @PostConstruct
    public void fakeData() {
        oauth2UserRepository.deleteAll();

        for (int i = 0; i < 10; i++) {
            Oauth2User user = new Oauth2User();
            user.setUsername("user" + i);
            user.setPassword(bCryptPasswordEncoder.encode("user" + i));
            user.setRoles(new ArrayList<Roles>() {{
                add(Roles.USER);
            }});
            oauth2UserRepository.save(user);
        }

        for (int i = 0; i < 10; i++) {
            Oauth2User user = new Oauth2User();
            user.setUsername("admin" + i);
            user.setPassword(bCryptPasswordEncoder.encode("admin" + i));
            user.setRoles(new ArrayList<Roles>() {{
                add(Roles.USER);
                add(Roles.ADMIN);
            }});
            oauth2UserRepository.save(user);
        }
    }
}
