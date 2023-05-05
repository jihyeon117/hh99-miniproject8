package com.example.hh99miniproject8.service;

import com.example.hh99miniproject8.dto.user.SignupReqeustDto;
import com.example.hh99miniproject8.dto.user.SignupResponseDto;
import com.example.hh99miniproject8.entity.RoleTypeEnum;
import com.example.hh99miniproject8.entity.User;
import com.example.hh99miniproject8.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private static final String ADMIN_TOKEN = "AAABnvxRVklrnYxKZ0aHgTBcXukeZygoC";
    public SignupResponseDto signup(SignupReqeustDto reqeust) {
        // 아이디 중복 체크
        if(userRepository.findByUsername(reqeust.getUsername()).isPresent())
            throw new IllegalArgumentException("중복된 아이디가 존재합니다.");

        RoleTypeEnum role = RoleTypeEnum.USER;
        // 관리자 인가 체크
        if(reqeust.isAdmin()){
            if(reqeust.getAuthKey() == null)
                throw new NullPointerException("관리자 인증키를 입력하지 않았습니다.");
            if(reqeust.getAuthKey().equals(ADMIN_TOKEN))
                role = RoleTypeEnum.ADMIN;
        }

        // 입력된 회원 정보 저장
        userRepository.save(new User(reqeust, role));
        return new SignupResponseDto("회원가입에 성공하셨습니다.");
    }
}
