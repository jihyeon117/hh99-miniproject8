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

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtUtil {
    public static final String AUTHORIZATION_KEY = "auth";
    public static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String BEARER_PREFIX = "Bearer ";
    // 만료 시간
    private static final long TOKEN_TIME = 60 * 60 * 1000L;

    @Value("${jwt.secret.key}")
    private String secretKey;

    private Key key;

    private final SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

    // 토큰 생성 메서드
    public String createToken(String username, RoleTypeEnum role){
        Date date = new Date();

        return BEARER_PREFIX +
                Jwts.builder()
                .setSubject(username)
                .claim(AUTHORIZATION_KEY, role)
                .setExpiration(new Date(date.getTime() + TOKEN_TIME))
                .setIssuedAt(date)
                .signWith(key,  signatureAlgorithm)
                .compact();
    }
    // 토큰 반환 메서드
    public String resolveToken(HttpServletRequest request){
        String bearerToken = request.getHeader(AUTHORIZATION_HEADER);
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(BEARER_PREFIX)){
            return bearerToken.substring(7);
        }
        return null;
    }

    // 토큰 유효성 검사 메서드
    public boolean vaildateToken(String token){
        try{
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

    @PostConstruct
    public void init(){
        byte[] bytes = Base64.getDecoder().decode(secretKey);
        key = Keys.hmacShaKeyFor(bytes);
    }
}
