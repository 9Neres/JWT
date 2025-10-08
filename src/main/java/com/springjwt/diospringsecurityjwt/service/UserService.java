package com.springjwt.diospringsecurityjwt.service;

import com.springjwt.diospringsecurityjwt.model.User;
import com.springjwt.diospringsecurityjwt.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository repository;

    @Autowired
    private PasswordEncoder encoder;

    public void createUser(User user){
        String pass = user.getPassword();

        // criptografia
        user.setPassword(encoder.encode(pass));
        repository.save(user);
    }
}
