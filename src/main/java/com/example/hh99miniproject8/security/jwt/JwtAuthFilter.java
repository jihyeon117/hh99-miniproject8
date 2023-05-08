package com.example.hh99miniproject8.security.jwt;

import io.jsonwebtoken.*;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
@Component
public class JwtAuthFilter extends OncePerRequestFilter {
    //OncePerRequestFilter : 요청들어 올때마다 한번씩 실행되는 Filter
    // 프론트 측에서 요청 헤더에 토큰을 넣어 보내면 이 필터가 검증해준다.
    private final JwtProvider jwtProvider;
    private final JwtService jwtService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // request의 header에서 accessToken을 받아온다.
        String accessToken = jwtProvider.resolveAccessToken(request);

        if (accessToken == null){       // 로그인 이전의 상태
            filterChain.doFilter(request, response);
            return;
        }
        else{// 로그인 이후의 상태
            if(jwtProvider.validateToken(accessToken)){ // 토큰이 유효하고 만료되지 않으면
                // jwtProvider.getUserInfoFromToken(accessToken) : token으로 claim을 반환함
                // jwtProvider.getAuthentication() : claim의 getsubject 즉 username을 매개변수로 user를 찾아 UserDetail을 생성,
                // UserDetails를 이용해서 Authentication객체를 만들어서 반환함.
                Authentication authentication = jwtProvider.getAuthentication(accessToken);
                // SecurityContext에 Authentication객체를 저장함
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }


        filterChain.doFilter(request, response);
    }

    private void setAuthentication(String username){
        SecurityContext context = SecurityContextHolder.createEmptyContext();       // SecurityContextHolder에서 빈 SecurityContext 객체 생성
        Authentication authentication = jwtService.createAuthentication(username);     // 인증에 성공한 user의 인증 객체 생성
        context.setAuthentication(authentication);                                  // 인증객체(Authentication)를 context에 set
        SecurityContextHolder.setContext(context);                                  // SecurityContext를 SecurityContextHolder에 set
    }
}
