package com.example.hh99miniproject8.service;

import com.example.hh99miniproject8.dto.user.LoginRequestDto;
import com.example.hh99miniproject8.dto.user.SignupRequestDto;
import com.example.hh99miniproject8.entity.RoleTypeEnum;
import com.example.hh99miniproject8.entity.User;
import com.example.hh99miniproject8.exception.CustomException;
import com.example.hh99miniproject8.exception.ErrorCode;
import com.example.hh99miniproject8.repository.UserRepository;
import com.example.hh99miniproject8.security.jwt.JwtUtil;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    private static final String ADMIN_TOKEN = "AAABnvxRVklrnYxKZ0aHgTBcXukeZygoC";
    public ResponseEntity<String> signup(SignupRequestDto reqeust) {
        // 아이디 중복 체크
        if(userRepository.findByUsername(reqeust.getUsername()).isPresent())
            throw new CustomException(ErrorCode.USER_Duplicate);

        RoleTypeEnum role = RoleTypeEnum.USER;
        // 관리자 인가 체크
        if(reqeust.isAdmin()){
            if(reqeust.getAuthKey() == null || !reqeust.getAuthKey().equals(ADMIN_TOKEN))
                throw new CustomException(ErrorCode.AUTHKEY_NOT_FOUND);
            if(reqeust.getAuthKey().equals(ADMIN_TOKEN))
                role = RoleTypeEnum.ADMIN;
        }

        // 입력된 회원 정보 저장
        userRepository.save(new User(reqeust, role));
        return ResponseEntity.status(HttpStatus.OK).body("회원가입 성공!");
    }

    public ResponseEntity<String> login(LoginRequestDto request, HttpServletResponse response) {
        // 입력한 ID를 기반으로 회원 존재 유무 체크
        User user = userRepository.findByUsername(request.getUsername()).orElseThrow(
                () -> new CustomException(ErrorCode.USER_NOT_FOUND)
        );

        // 비밀번호 일치여부 체크
        if(!user.getPassword().equals(request.getPassword())){
            throw new CustomException(ErrorCode.INVALIED_PASSWORD);
        }

        response.addHeader(JwtUtil.AUTHORIZATION_HEADER, jwtUtil.createToken(user.getUsername(), user.getRole()));
        return ResponseEntity.status(HttpStatus.OK).body("로그인 성공!");
    }
}
