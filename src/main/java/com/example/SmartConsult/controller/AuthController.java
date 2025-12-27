package com.example.SmartConsult.controller;

// import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.SmartConsult.dto.LoginRequest;
import com.example.SmartConsult.dto.LoginResponse;
import com.example.SmartConsult.dto.RegisterRequest;
import com.example.SmartConsult.service.AuthService;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthService authService;

    public AuthController(AuthService authService){
        this.authService=authService;
    }


    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody RegisterRequest request){
        authService.register(request);
        return ResponseEntity
        .status(HttpStatus.CREATED)
        .body("User registered successfully");
    }

// @PostMapping("/register")
// public String debug(@RequestBody Object body) {
//     return body.toString();
// }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request){
        LoginResponse response=authService.login(request);
        return ResponseEntity.ok(response);
    }




    
}
