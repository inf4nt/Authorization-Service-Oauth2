package com.authorization.service.server.model;

import com.authorization.service.server.common.Roles;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document(collection = "oauth2_user")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Oauth2User {

    @Id
    String id;

    @Indexed(unique = true)
    String username;

    String password;

    List<Roles> roles;
}
