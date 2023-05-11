package com.example.hh99miniproject8.controller;

import com.example.hh99miniproject8.security.jwt.UserDetailsImpl;
import com.example.hh99miniproject8.service.GoodService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping
@CrossOrigin
public class GoodController {

    private final GoodService goodService;

    @PostMapping("/goods/{id}")
    public ResponseEntity<String> likes(@PathVariable Long id, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return goodService.likes(id, userDetails.getUser());
    }

}
