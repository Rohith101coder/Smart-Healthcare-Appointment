package com.example.SmartConsult.service;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.example.SmartConsult.dto.LoginRequest;
import com.example.SmartConsult.dto.RegisterRequest;
import com.example.SmartConsult.entity.Role;
import com.example.SmartConsult.entity.User;
import com.example.SmartConsult.exception.InvalidCredentialsException;
import com.example.SmartConsult.exception.ResourceNotFoundException;
import com.example.SmartConsult.repository.UserRepository;
import com.example.SmartConsult.security.JwtService;

@ExtendWith(MockitoExtension.class)
public class AuthServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtService jwtService;

    @InjectMocks
    private AuthService authService;

    private RegisterRequest registerRequest;
    private LoginRequest loginRequest;
    private User user;

    @BeforeEach
    public void setUp() {
        registerRequest = new RegisterRequest();
        registerRequest.setName("John Doe");
        registerRequest.setEmail("john@example.com");
        registerRequest.setPassword("password123");
        registerRequest.setRole(Role.PATIENT);

        loginRequest = new LoginRequest();
        loginRequest.setEmail("john@example.com");
        loginRequest.setPassword("password123");

        user = new User();
        user.setId(1L);
        user.setName("John Doe");
        user.setEmail("john@example.com");
        user.setPassword("encodedPassword");
        user.setRole(Role.PATIENT);
    }

    @Test
    public void testRegisterUserSuccess() {
        when(userRepository.existsByEmail(registerRequest.getEmail())).thenReturn(false);

        authService.register(registerRequest);

        verify(userRepository).save(any(User.class));
    }

    @Test
    public void testRegisterUserEmailAlreadyExists() {
        when(userRepository.existsByEmail(registerRequest.getEmail())).thenReturn(true);

        assertThrows(ResourceNotFoundException.class, () -> authService.register(registerRequest));
    }

    @Test
    public void testLoginSuccess() {
        when(userRepository.findByEmail(loginRequest.getEmail())).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())).thenReturn(true);
        when(jwtService.generateToken(user.getEmail(), user.getRole().name())).thenReturn("token123");

        authService.login(loginRequest);

        verify(jwtService).generateToken(user.getEmail(), user.getRole().name());
    }

    @Test
    public void testLoginUserNotFound() {
        when(userRepository.findByEmail(loginRequest.getEmail())).thenReturn(Optional.empty());

        assertThrows(InvalidCredentialsException.class, () -> authService.login(loginRequest));
    }

    @Test
    public void testLoginInvalidPassword() {
        when(userRepository.findByEmail(loginRequest.getEmail())).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())).thenReturn(false);

        assertThrows(InvalidCredentialsException.class, () -> authService.login(loginRequest));
    }
}
