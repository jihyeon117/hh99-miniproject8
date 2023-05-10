package com.example.hh99miniproject8.service;

import com.example.hh99miniproject8.entity.Good;
import com.example.hh99miniproject8.entity.Post;
import com.example.hh99miniproject8.entity.User;
import com.example.hh99miniproject8.exception.CustomException;
import com.example.hh99miniproject8.repository.GoodRepository;
import com.example.hh99miniproject8.repository.PostRepository;
import com.example.hh99miniproject8.repository.UserRepository;
import com.example.hh99miniproject8.security.jwt.JwtProvider;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.example.hh99miniproject8.exception.ErrorCode.*;

@Service
@RequiredArgsConstructor
public class GoodService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final GoodRepository goodRepository;
    private final JwtProvider jwtProvider;

    @Transactional
    public ResponseEntity<String> likes(Long id, HttpServletRequest httpServletRequest) {

        User user = tokenCheck(httpServletRequest);
        Post post = postRepository.findById(id).orElseThrow(
                () -> new CustomException(POST_NOT_FOUND)
        );

        if (goodRepository.findByPostAndUser(post, user) == null) {
            goodRepository.save(new Good(post, user));
            post.togglLike(true);
            return ResponseEntity.status(HttpStatus.OK).body("게시글 좋아요 등록!");
        } else {
            Good good = goodRepository.findByPostAndUser(post, user);
            goodRepository.delete(good);
            post.togglLike(false);
            return ResponseEntity.status(HttpStatus.OK).body("게시글 좋아요 취소!");
        }
    }

    // 토큰 검사
    public User tokenCheck(HttpServletRequest httpServletRequest) {
        String token = jwtProvider.resolveAccessToken(httpServletRequest);
        Claims claims;

        if (token != null) {
            if (jwtProvider.validateToken(token)) {
                claims = jwtProvider.getUserInfoFromToken(token);
            } else {
                throw new CustomException(TOKEN_NOT_FOUND);
            }
            User user = userRepository.findByUsername(claims.getSubject()).orElseThrow(
                    () -> new CustomException(USER_NOT_FOUND)
            );
            return user;
        }
        return null;
    }
}
