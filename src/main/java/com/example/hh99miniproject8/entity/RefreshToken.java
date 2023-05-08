package com.example.hh99miniproject8.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class RefreshToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "REFRESHTOKEN_ID")
    private Long id;

    @Column
    private String refreshToken;

    public RefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }
}
