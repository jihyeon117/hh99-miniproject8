package com.example.hh99miniproject8.service;

import com.example.hh99miniproject8.dto.user.LoginRequestDto;
import com.example.hh99miniproject8.dto.user.SignupRequestDto;
import com.example.hh99miniproject8.entity.RoleTypeEnum;
import com.example.hh99miniproject8.entity.Token;
import com.example.hh99miniproject8.entity.User;
import com.example.hh99miniproject8.exception.CustomException;
import com.example.hh99miniproject8.exception.ErrorCode;
import com.example.hh99miniproject8.repository.UserRepository;
import com.example.hh99miniproject8.security.jwt.JwtProvider;
import com.example.hh99miniproject8.security.jwt.JwtService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final JwtProvider jwtProvider;

    private static final String ADMIN_TOKEN = "AAABnvxRVklrnYxKZ0aHgTBcXukeZygoC";
    public ResponseEntity<String> signup(SignupRequestDto reqeust) {
        // 아이디 중복 체크
        if(userRepository.findByUsername(reqeust.getUsername()).isPresent())
            throw new CustomException(ErrorCode.USER_Duplicate);

        RoleTypeEnum role = RoleTypeEnum.USER;
        // 관리자 인가 체크
        if(reqeust.isAdmin()){
            // 입력받은 isAdmin이 True 이면 ADMIN으로 가입
            // AtuhKey가 null 이면 즉 아무것도 입력하지 않으면 AUTHKEY_NOT_FOUND(입력하지 않음) 예외
            // AtuhKey가 null이 아니고 서버의 ADMIN_TOKEN과 비교해서 같을 떄 관리자 가입 인증완료
            if(reqeust.getAuthKey() == null)                 // 아무것도 입력하지 않았을 때)
                throw new CustomException(ErrorCode.AUTHKEY_NOT_FOUND);

            if(reqeust.getAuthKey().equals((ADMIN_TOKEN)))  // 입력한 키값이 틀릴때
                throw new CustomException((ErrorCode.INVALIED_AUTHKEY));

            if(reqeust.getAuthKey().equals(ADMIN_TOKEN))
                role = RoleTypeEnum.ADMIN;
        }

        // 입력된 회원 정보 저장
        userRepository.save(new User(reqeust, role));
        return ResponseEntity.status(HttpStatus.OK).body("회원가입 성공!");
    }

    @Transactional
    public ResponseEntity<Token> login(LoginRequestDto request, HttpServletResponse response) {
        // 입력한 ID를 기반으로 회원 존재 유무 체크
        User user = userRepository.findByUsername(request.getUsername()).orElseThrow(
                () -> new CustomException(ErrorCode.USER_NOT_FOUND)
        );
        // 비밀번호 일치여부 체크
        if(!user.getPassword().equals(request.getPassword())){
            throw new CustomException(ErrorCode.INVALIED_PASSWORD);
        }

        // accesstoken, refreshtoken 발급
        Token tokenDto = jwtProvider.createToken(user.getUsername(), user.getRole());
        String accessToken = tokenDto.getAccessToken();
        String refreshToken = tokenDto.getRefreshToken();

        // 로그인 성공시 client에게 acces, refresh 토큰 header에 넣어서 반환
        // header에서 꺼내서 사용할떄 ex) response.getHeader("AUTHORIZATION");
        response.setHeader("AUTHORIZATION", accessToken);
        response.setHeader("REFRESHTOKEN", refreshToken);

        // 로그인 성공시 해당 user에게 refreshToken값의 상태를 주입 (로그인 상태 저장)
        jwtService.setRefreshToken(user, refreshToken);

//        response.addHeader(JwtProvider.AUTHORIZATION_HEADER, accessToken, refreshToken);
        return ResponseEntity.status(HttpStatus.OK).body(tokenDto);
    }
}
