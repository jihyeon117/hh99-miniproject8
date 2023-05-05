package com.example.hh99miniproject8.controller;

import com.example.hh99miniproject8.dto.user.LoginReqeustDto;
import com.example.hh99miniproject8.dto.user.SignupReqeustDto;
import com.example.hh99miniproject8.dto.user.SignupResponseDto;
import com.example.hh99miniproject8.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/signup")
    public SignupResponseDto signup(@RequestBody SignupReqeustDto reqeust){
        return userService.signup(reqeust);
    }

    @PostMapping("/login")
    public void login(@RequestBody LoginReqeustDto reqeust){
        userService.login(reqeust);
    }
}
