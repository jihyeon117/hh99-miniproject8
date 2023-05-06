package com.example.hh99miniproject8.security.jwt;

import com.example.hh99miniproject8.entity.RoleTypeEnum;
import com.example.hh99miniproject8.entity.User;
import com.example.hh99miniproject8.exception.CustomException;
import com.example.hh99miniproject8.exception.ErrorCode;
import com.example.hh99miniproject8.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class JwtService {
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;

    // RefreshToken으로 부터 user의 정보를 반환하는 메서드
    public User getMemberByRefreshToken(String token){
        return userRepository.findByRefreshtoken(token).orElseThrow(
                () -> new CustomException(ErrorCode.JWT_REFRESH_EXPRIED));
    }

    // User의 RefreshToken을 설정해주는 메서드
    // request한 user가 존재하는지 검사하고 존재한다면 refreshJwt를 설정해준다.
    public void setRefreshToken(String username, String refreshJwt){
        userRepository.findByUsername(username).ifPresent(user -> user.setRefreshToken(refreshJwt));
    }

    // 로그아웃시 User의 RefreshToken를 삭제하는 메서드
    public void removeRefreshToken(String token){
        userRepository.findByRefreshtoken(token).ifPresent(user -> user.setRefreshToken(null));
    }

    // accesstoken 생성
    public String createAccessToken(String username, RoleTypeEnum role){
        return jwtUtil.createAccessToken(username, role);
    }

    // refreshtoken 생성
    public String createRefreshToken(){
        return jwtUtil.createRefreshToken();
    }

    // token의 유효성 검사
    public void isVaildToken(String token){
        jwtUtil.validateToken(token);
    }

    // token의 상태 검사를 하는 메서드
    // 1. ACCESS 0  REFREST 0  => 로그인 직후 시점
    // 2. ACCESS X  REFREST 0  => ACCESS 토큰이 만료된 시점 (REFREST를 검증해서 ACCESS 재발급)
    // 3. ACCESS 0  REFREST X  => REFREST가 만료되었으나 ACCESS가 인증된 상태 (REFREST 재발급)
    // 4. ACCESS X  REFREST X  => 로그아웃

}
