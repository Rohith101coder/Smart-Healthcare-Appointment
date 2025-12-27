package com.example.SmartConsult.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.SmartConsult.dto.LoginRequest;
import com.example.SmartConsult.dto.LoginResponse;
import com.example.SmartConsult.dto.RegisterRequest;
import com.example.SmartConsult.entity.User;
import com.example.SmartConsult.repository.UserRepository;
import com.example.SmartConsult.security.JwtService;

@Service
public class AuthService {
    

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthService(UserRepository userRepository,PasswordEncoder passwordEncoder,JwtService jwtService){
        this.userRepository=userRepository;
        this.passwordEncoder=passwordEncoder;
        this.jwtService=jwtService;
    }

    public void register(RegisterRequest request){
        if(userRepository.existsByEmail(request.getEmail())){
            throw new RuntimeException("Email already registered");
        }
        User user=new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());

        user.setPassword(passwordEncoder.encode(request.getPassword()));

        user.setRole(request.getRole());

        userRepository.save(user);
    }

    public LoginResponse login(LoginRequest request){

        //1. find user by email
        User user=userRepository.findByEmail(request.getEmail())
                                        .orElseThrow(()->new RuntimeException("Invalid email or password "));

        //2.validate password
        if(!passwordEncoder.matches(request.getPassword(),user.getPassword())){
            throw new RuntimeException("Invalid emial or password");
        }

        //3. generate JwT
        String token=jwtService.generateToken(user.getEmail(), user.getRole().name());

        return new LoginResponse(token);

    }
}
