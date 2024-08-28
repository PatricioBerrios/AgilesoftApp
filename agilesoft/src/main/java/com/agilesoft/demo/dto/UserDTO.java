package com.agilesoft.demo.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Getter
@Setter
public class UserDTO {

    private String username;
    private String password;
    private String name;

}
