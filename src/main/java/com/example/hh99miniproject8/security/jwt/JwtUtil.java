package com.example.hh99miniproject8.security.jwt;

import com.example.hh99miniproject8.entity.RoleTypeEnum;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.security.Key;
import java.util.Base64;
import java.util.Date;


// 토큰의 생성, 반환, 유효성 검사만을 담당하는 component
@Slf4j
@Component
@RequiredArgsConstructor
public class JwtUtil {
    public static final String AUTHORIZATION_KEY = "auth";
//    public static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String BEARER_PREFIX = "Bearer ";
    // 만료 시간
    private static final long ACCESS_TOKEN_TIME = 60 * 60 * 1000L;              // ACCESS_TOKEN 만료시간 1시간
    private static final long REFRESH_TOKEN_TIME = ACCESS_TOKEN_TIME * 24 * 14; // REFRESH_TOKEN 만료시간 보통 2주로 설정


    @Value("${jwt.secret.key}")
    private String secretKey;

    private Key key;

    private final SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

    @PostConstruct
    public void init() {
        byte[] bytes = Base64.getDecoder().decode(secretKey);
        key = Keys.hmacShaKeyFor(bytes);
    }

    // 토큰 생성 메서드
    // ACCESSTOKEN과 REFRESHTOKEN을 동시에 만들어서 MAP으로 반환한다.
    // MAP으로 하면 매번 api인증이 들어왔을때 key(REFRESHTOKEN)를 통해 value(ACCESSTOKEN과)를 인증해야하니 부적합하다고 생각해서 바꿈...
    public String createAccessToken(String username, RoleTypeEnum role){
        Date date = new Date();

        return BEARER_PREFIX +
                Jwts.builder()
                        .setSubject(username)
                        .claim(AUTHORIZATION_KEY, role)
                        .setExpiration(new Date(date.getTime() + ACCESS_TOKEN_TIME))
                        .setIssuedAt(date)
                        .signWith(key,  signatureAlgorithm)
                        .compact();
    }

    public String createRefreshToken(){
        Date date = new Date();

        return Jwts.builder()
                        .setSubject("refresh")
                        .setExpiration(new Date(date.getTime() + REFRESH_TOKEN_TIME))
                        .setIssuedAt(date)
                        .signWith(key,  signatureAlgorithm)
                        .compact();
    }
    // ACCESS 토큰 반환 메서드
    public String resolveAccessToken(HttpServletRequest request){
        String bearerToken = request.getHeader("AUTHORIZATION");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(BEARER_PREFIX)){
            return bearerToken.substring(7);
        }
        return null;
    }

    // 토큰 유효성 검사 메서드
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (SecurityException | MalformedJwtException e) {
            log.info("Invalid JWT signature, 유효하지 않는 JWT 서명 입니다.");
        } catch (ExpiredJwtException e) {
            log.info("Expired JWT token, 만료된 JWT token 입니다.");
        } catch (UnsupportedJwtException e) {
            log.info("Unsupported JWT token, 지원되지 않는 JWT 토큰 입니다.");
        } catch (IllegalArgumentException e) {
            log.info("JWT claims is empty, 잘못된 JWT 토큰 입니다.");
        }
        return false;
    }

    // 토큰에서 사용자 정보 반환
    public Claims getUserInfoFromToken(String token){
        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
    }


}
