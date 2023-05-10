package com.example.hh99miniproject8.controller;

import com.example.hh99miniproject8.service.GoodService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping
@CrossOrigin
public class GoodController {

    private final GoodService goodService;

    @PostMapping("/goods/{id}")
    public ResponseEntity<String> likes(@PathVariable Long id, HttpServletRequest httpServletRequest) {
        return goodService.likes(id, httpServletRequest);
    }

}
