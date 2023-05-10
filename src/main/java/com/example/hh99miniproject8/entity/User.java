package com.example.hh99miniproject8.entity;

import com.example.hh99miniproject8.dto.user.SignupRequestDto;
import jakarta.persistence.*;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

@Entity(name = "users")
@NoArgsConstructor
@Getter
public class User {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "userid", nullable = false, unique = true, updatable = false)
    private Long id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    private RoleTypeEnum role;

    @Column(nullable = false)
    private String address;

    @Column(nullable = false)
    private String nickname;

    private String refreshToken;

//    @OneToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "REFRESHTOKEN_ID")
//    private RefreshToken refreshToken;

    public User(SignupRequestDto requst, RoleTypeEnum role) {
        this.username = requst.getUsername();
        this.password = requst.getPassword();
        this.role = role;
        this.address = requst.getAddress();
        this.nickname = requst.getNickname();
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }
}
