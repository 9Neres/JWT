package com.springjwt.diospringsecurityjwt.controller;


import com.springjwt.diospringsecurityjwt.model.User;
import com.springjwt.diospringsecurityjwt.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    public UserService userService;

    @PostMapping
    public void postUser(@RequestBody User user){
        userService.createUser(user);
    }
}
