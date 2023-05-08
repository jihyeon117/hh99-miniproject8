package com.example.hh99miniproject8.security.jwt;

import com.example.hh99miniproject8.entity.RoleTypeEnum;
import com.example.hh99miniproject8.entity.Token;
import com.example.hh99miniproject8.entity.User;
import com.example.hh99miniproject8.exception.CustomException;
import com.example.hh99miniproject8.exception.ErrorCode;
import com.example.hh99miniproject8.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class  JwtService {
    private final UserRepository userRepository;
    private final UserDetailsServiceImpl userDetailsService;
    private final JwtProvider jwtProvider;

    // RefreshToken으로 부터 user의 정보를 반환하는 메서드
    public User getMemberByRefreshToken(String token){
        return userRepository.findByRefreshToken(token).orElseThrow(
                () -> new CustomException(ErrorCode.JWT_REFRESH_EXPRIED));
    }

    // User의 RefreshToken을 설정해주는 메서드
    // request한 user가 존재하는지 검사하고 존재한다면 refreshJwt를 설정해준다.
    public void setRefreshToken(User user, String refreshJwt){
        user.setRefreshToken(refreshJwt);
    }

    // 로그아웃시 User의 RefreshToken를 삭제하는 메서드
    public void removeRefreshToken(String token){
        userRepository.findByRefreshToken(token).ifPresent(user -> user.setRefreshToken(null));
    }

    // accessToken으로 부터 user를 반환하는 메서드
//    public String getUserByAccessToken(String username){
//        User user = userRepository.findByUsername(username).get();  // AccessToken으로 부터 user를 검색
//        if(!jwtProvider.validateToken(user.getRefreshToken())){ // AccessToken, RefeshToken 둘다 만료되었을 때
//            log.info("로그인을 다시 해주세요");
//        }
//        return jwtProvider.createAccessToken(user.getUsername(), user.getRole());
//    }

    // refreshtoken 생성
//    public String createRefreshToken(){
//        return jwtProvider.createRefreshToken();
//    }

    public Authentication createAuthentication(String username) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
    }

    // token의 상태 검사를 하는 메서드
    // 1. ACCESS 0  REFREST 0  => 로그인 직후 시점
    // 2. ACCESS X  REFREST 0  => ACCESS 토큰이 만료된 시점 (REFREST를 검증해서 ACCESS 재발급)
    // 3. ACCESS 0  REFREST X  => REFREST가 만료되었으나 ACCESS가 인증된 상태 (REFREST 재발급)
    // 4. ACCESS X  REFREST X  => 로그아웃

}
