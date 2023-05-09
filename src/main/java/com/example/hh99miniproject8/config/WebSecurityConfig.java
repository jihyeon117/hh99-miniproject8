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
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration              // 스프링 컨텍스트에서 빈으로 등록되는 클래스를 선언합니다.
@Slf4j                      // lombok 어노테이션으로, 자동으로 로깅 코드를 생성합니다.
@RequiredArgsConstructor    // lombok 어노테이션으로, final 필드를 가진 생성자를 자동으로 생성합니다.
@EnableWebSecurity          // Spring Security를 사용하기 위한 어노테이션입니다.
public class WebSecurityConfig {
    private final JwtProvider jwtProvider;
    private final JwtService jwtService;

    @Bean
    public PasswordEncoder passwordEncoder() {
        log.info("encode setting");
        return new BCryptPasswordEncoder();
    }    // BCryptPasswordEncoder를 사용하여 입력받은 값을 단방향 해시화합니다.

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer(){
        log.info("web ignore setting");
        return (web) -> web.ignoring()
                .requestMatchers("/signup")
                .requestMatchers(PathRequest.toH2Console())                             // h2db 인증 요청 무시
                .requestMatchers(PathRequest.toStaticResources().atCommonLocations()); // staticResource(images, js, css등등) 인증 절차 무시
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
        log.info("Filter setting");
        http.csrf().disable();

        // cors 설정!! 이걸 안해놓으면 아래의 corsConfigurationSource()가 적용이 안된다.!!
        // http.cors();
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

    // 이 설정을 해주면, 우리가 설정한대로 CorsFilter가 Security의 filter에 추가되어
    // 예비 요청에 대한 처리를 해주게 됩니다.
    // CorsFilter의 동작 과정이 궁금하시면, CorsFilter의 소스코드를 들어가 보세요!
    @Bean
    public CorsConfigurationSource corsConfigurationSource(){

        CorsConfiguration config = new CorsConfiguration();

        // 사전에 약속된 출처를 명시
        // TEST CODE라서 "http://localhost:3000"로 명시해놨다 이부분 수정해서 사용하자!
        config.addAllowedOrigin("http://localhost:3000");

        // 특정 헤더를 클라이언트 측에서 사용할 수 있게 지정
        // 만약 지정하지 않는다면, Authorization 헤더 내의 토큰 값을 사용할 수 없음
        config.addExposedHeader(JwtProvider.ACCESS_AUTHORIZATION_HEADER);

        // 본 요청에 허용할 HTTP method(예비 요청에 대한 응답 헤더에 추가됨)
        config.addAllowedMethod("*");

        // 본 요청에 허용할 HTTP header(예비 요청에 대한 응답 헤더에 추가됨)
        config.addAllowedHeader("*");

        // 기본적으로 브라우저에서 인증 관련 정보들을 요청 헤더에 담지 않음
        // 이 설정을 통해서 브라우저에서 인증 관련 정보들을 요청 헤더에 담을 수 있도록 해줍니다.
        config.setAllowCredentials(true);

        // allowCredentials 를 true로 하였을 때,
        // allowedOrigin의 값이 * (즉, 모두 허용)이 설정될 수 없도록 검증합니다.
        config.validateAllowCredentials();

        // 어떤 경로에 이 설정을 적용할 지 명시합니다. (여기서는 전체 경로)
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);

        return source;
    }
}
