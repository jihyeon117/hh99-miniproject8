package com.example.hh99miniproject8.config;

import com.example.hh99miniproject8.security.jwt.JwtAuthFilter;
import com.example.hh99miniproject8.security.jwt.JwtProvider;
import com.example.hh99miniproject8.security.jwt.JwtService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@Slf4j
@RequiredArgsConstructor
@EnableWebSecurity
public class WebSecurityConfig {
    private final JwtProvider jwtProvider;
    private final JwtService jwtService;

    @Bean
    public PasswordEncoder passwordEncoder() {
        log.info("encode setting");
        return new BCryptPasswordEncoder();
    }    // 단방향 암호화 인코더

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer(){
        log.info("web ignore setting");
        return (web -> web.ignoring()
                .requestMatchers(PathRequest.toH2Console())                             // h2db 인증 요청 무시
                .requestMatchers(PathRequest.toStaticResources().atCommonLocations())); // staticResource(images, js, css등등) 인증 절차 무시
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
        log.info("Filter setting");
        http.csrf().disable();
        http.authorizeHttpRequests()
                //>>>>>>>>>>>>>>>>>>>> 인증을 무시하는 reqeust
                .requestMatchers("/signup", "/login").permitAll()
                //<<<<<<<<<<<<<<<<<<<< 인증을 받아야하는 request
                .anyRequest().authenticated()

                // JwtToken을 인증할 Custom Filter 삽입.
                .and().addFilterBefore(new JwtAuthFilter(jwtProvider, jwtService), UsernamePasswordAuthenticationFilter.class);

        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        // 세션을 사용하지 않도록 설정.

        return http.build();
    }
}
