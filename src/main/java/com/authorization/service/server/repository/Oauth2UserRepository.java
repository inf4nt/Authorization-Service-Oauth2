package com.authorization.service.server.repository;

import com.authorization.service.server.model.Oauth2User;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface Oauth2UserRepository extends MongoRepository<Oauth2User, String> {

    Oauth2User findByUsername(String username);
}
