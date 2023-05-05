package com.example.hh99miniproject8.entity;

import com.example.hh99miniproject8.dto.user.SignupRequestDto;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity(name = "users")
@NoArgsConstructor
@Getter
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String username;
    private String password;
    @Enumerated(EnumType.STRING)
    private RoleTypeEnum role;
    private String address;
    private String nickname;

    public User(SignupRequestDto requst, RoleTypeEnum role) {
        this.username = requst.getUsername();
        this.password = requst.getPassword();
        this.role = role;
        this.address = requst.getAddress();
        this.nickname = requst.getNickname();
    }
}
