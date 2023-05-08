package com.example.hh99miniproject8.security.jwt;

import com.example.hh99miniproject8.entity.RefreshToken;
import com.example.hh99miniproject8.entity.RoleTypeEnum;
import com.example.hh99miniproject8.entity.Token;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.security.Key;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.Objects;


// 토큰의 생성, 반환, 유효성 검사만을 담당하는 component
@Slf4j
@Component
@RequiredArgsConstructor
public class JwtProvider {
    private final UserDetailsService userDetailsService;
    public static final String AUTHORIZATION_KEY = "auth";
//    public static final String AUTHORIZATION_HEADER = "Authorization";
    public static final String BEARER_PREFIX = "Bearer ";
    // 만료 시간
    private static final long ACCESS_TOKEN_TIME = 30 * 60 * 1000L;              // ACCESS_TOKEN 만료시간 30분
    private static final long REFRESH_TOKEN_TIME = ACCESS_TOKEN_TIME * 24 * 14; // REFRESH_TOKEN 만료시간 보통 2주로 설정

    @Value("${jwt.secret.key}")
    private String secretKey;

    public static Key key;

    private final SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

    @PostConstruct
    public void init() {
        byte[] bytes = Base64.getDecoder().decode(secretKey);       // jwt key를 암호화
        key = Keys.hmacShaKeyFor(bytes);
    }

    // 토큰 생성 메서드
    // ACCESSTOKEN과 REFRESHTOKEN을 동시에 만들어서 MAP으로 반환한다.
    // MAP으로 하면 매번 api인증이 들어왔을때 key(REFRESHTOKEN)를 통해 value(ACCESSTOKEN과)를 인증해야하니 부적합하다고 생각해서 바꿈...
    public Token createToken(String username, RoleTypeEnum role){
        Date now = new Date();

        //Access Token
        String accessToken = BEARER_PREFIX +
                Jwts.builder()
                .setSubject(username)
                .claim(AUTHORIZATION_KEY, role)
                .setIssuedAt(now) // 토큰 발행 시간 정보
                .setExpiration(new Date(now.getTime() + ACCESS_TOKEN_TIME)) // set Expire Time
                .signWith(SignatureAlgorithm.HS256, key)  // 사용할 암호화 알고리즘과
                // signature 에 들어갈 secret값 세팅
                .compact();

        //Refresh Token
        String refreshToken =  Jwts.builder()
                .setSubject(username)
                .claim(AUTHORIZATION_KEY, role)
                .setIssuedAt(now) // 토큰 발행 시간 정보
                .setExpiration(new Date(now.getTime() + REFRESH_TOKEN_TIME)) // set Expire Time
                .signWith(SignatureAlgorithm.HS256, key)  // 사용할 암호화 알고리즘과
                // signature 에 들어갈 secret값 세팅
                .compact();

        return Token.builder().accessToken(accessToken).refreshToken(refreshToken).key(username).build();
    }

    public String recreationAccessToken(String username, String role){
        Date date = new Date();
        // new Access Token
        return BEARER_PREFIX +
                Jwts.builder()
                .setSubject(username)
                .claim(AUTHORIZATION_KEY, role)
                .setIssuedAt(date) // 토큰 발행 시간 정보
                .setExpiration(new Date(date.getTime() + ACCESS_TOKEN_TIME)) // set Expire Time
                .signWith(SignatureAlgorithm.HS256, key)  // 사용할 암호화 알고리즘과
                // signature 에 들어갈 secret값 세팅
                .compact();
    }

//    public String createRefreshToken(){
//        Date date = new Date();
//
//        return Jwts.builder()
//                        .setSubject("refresh")
//                        .setExpiration(new Date(date.getTime() + REFRESH_TOKEN_TIME))
//                        .setIssuedAt(date)
//                        .signWith(key,  signatureAlgorithm)
//                        .compact();
//    }

    // ACCESS 토큰 반환 메서드
    public String resolveAccessToken(HttpServletRequest request){
        String bearerToken = request.getHeader("AUTHORIZATION");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(BEARER_PREFIX)){
            return bearerToken.substring(7);
        }
        return null;
    }

//    public String resolveRefreshToken(HttpServletRequest request){
//        String bearerToken = request.getHeader("REFRESHTOKEN");
//        if (StringUtils.hasText(bearerToken)){
//            return bearerToken;
//        }
//        return null;
//    }

    // 토큰 유효성 검사 메서드
    public boolean validateToken(String token) {
        try {
            Jws<Claims> claims = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            // parserBuilder() : jwtTokenBuiler를 반환
            // setSigningKey   : signatureAlgorithm에 사용할 key를 setting
            // parseClaimsJws(token)
            return !claims.getBody().getExpiration().before(new Date());
        } catch (SecurityException | MalformedJwtException e) {
            log.info("Invalid JWT signature, 유효하지 않는 JWT 서명 입니다.");
        } catch (ExpiredJwtException e) {
            log.info("Expired JWT token, 만료된 JWT token 입니다.");
        } catch (UnsupportedJwtException e) {
            log.info("Unsupported JWT token, 지원되지 않는 JWT 토큰 입니다.");
        } catch (IllegalArgumentException e) {  //if the claimsJws string is null or empty or only whitespace
            log.info("JWT claims is empty, 잘못된 JWT 토큰 입니다.");
        }
        return false;
    }

    public String validateRefreshToken(RefreshToken refreshTokenObj){
        // refresh 객체에서 refreshToken 추출
        String refreshToken = refreshTokenObj.getRefreshToken();
        try {
            // 검증
            Jws<Claims> claims = Jwts.parser().setSigningKey(key).parseClaimsJws(refreshToken);

            //refresh 토큰의 만료시간이 지나지 않았을 경우, 새로운 access 토큰을 생성합니다.
            if (!claims.getBody().getExpiration().before(new Date())) {
                Claims claimsOfToken = claims.getBody();
                return recreationAccessToken(claimsOfToken.getSubject(), claims.getBody().get("auth").toString());
            }
        }catch (Exception e) {
            //refresh 토큰이 만료되었을 경우, 로그인이 필요합니다.
            return null;
        }
        return null;
    }


    // 토큰에서 사용자 정보 반환
    public Claims getUserInfoFromToken(String token){
        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
    }

    // JWT 토큰에서 인증 정보 조회
    public Authentication getAuthentication(String token) {
        //토큰 복호화
        Claims claims = getUserInfoFromToken(token);
        UserDetails userDetails = userDetailsService.loadUserByUsername(claims.getSubject());
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }


}
