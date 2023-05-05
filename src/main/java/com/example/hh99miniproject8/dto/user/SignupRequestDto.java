package com.example.hh99miniproject8.dto.user;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SignupRequestDto {
    private String username;
    private String password;
    private String nickname;
    private String address;
    private String authKey;
    private boolean isAdmin;
}
