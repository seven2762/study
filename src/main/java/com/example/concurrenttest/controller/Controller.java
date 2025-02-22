package com.example.concurrenttest.controller;

import com.example.concurrenttest.dto.CreateUserReq;
import com.example.concurrenttest.dto.UserRes;
import com.example.concurrenttest.service.OrderService;
import com.example.concurrenttest.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class Controller {

    private final UserService userService;


    @PostMapping("/signUp")
    public ResponseEntity<UserRes> signUp(@RequestBody CreateUserReq req) {
       UserRes response =  userService.createUser(req);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
