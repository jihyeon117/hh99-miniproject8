package com.example.hh99miniproject8.controller;

import com.example.hh99miniproject8.dto.user.LoginRequestDto;
import com.example.hh99miniproject8.dto.user.SignupRequestDto;
import com.example.hh99miniproject8.entity.Token;
import com.example.hh99miniproject8.service.UserService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UserController{
    private final UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<String> signup(@RequestBody SignupRequestDto request){
        return userService.signup(request);
    }

    @PostMapping("/login")
    public ResponseEntity<Token> login(@RequestBody LoginRequestDto request, HttpServletResponse httpResponse){
       return userService.login(request, httpResponse);
    }
}
