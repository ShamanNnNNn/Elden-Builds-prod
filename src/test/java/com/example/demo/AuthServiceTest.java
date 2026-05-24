package com.example.demo;

import com.example.demo.dto.AuthRequest;
import com.example.demo.dto.AuthResponse;
import com.example.demo.dto.RefreshTokenRequest;
import com.example.demo.model.RefreshToken;
import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.AuthService;
import com.example.demo.service.JwtService;
import com.example.demo.service.RefreshTokenService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JwtService jwtService;

    @Mock
    private RefreshTokenService refreshTokenService;

    @Mock
    private Authentication authentication;

    @InjectMocks
    private AuthService authService;


    @Test
    void register_ShouldReturnAuthResponse_WhenSuccess() {
        AuthRequest request = new AuthRequest();
        request.setEmail("newuser@test.com");
        request.setPassword("password123");

        String encodedPassword = "encodedPassword";
        String accessToken = "jwt.access.token";
        UUID refreshTokenUuid = UUID.randomUUID();

        User user = new User();
        user.setId(1L);
        user.setEmail(request.getEmail());
        user.setPassword(encodedPassword);
        user.setRole(User.Role.USER);

        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setToken(refreshTokenUuid);
        refreshToken.setUser(user);

        when(userRepository.findByEmail(request.getEmail())).thenReturn(Optional.empty());
        when(passwordEncoder.encode(request.getPassword())).thenReturn(encodedPassword);
        when(userRepository.save(any(User.class))).thenReturn(user);
        when(refreshTokenService.createRefreshToken(any(User.class))).thenReturn(refreshToken);
        when(jwtService.generateToken(any(User.class))).thenReturn(accessToken);

        AuthResponse response = authService.register(request);

        assertNotNull(response);
        assertEquals(accessToken, response.getAccessToken());
        assertEquals(refreshTokenUuid.toString(), response.getRefreshToken());

        verify(userRepository).findByEmail(request.getEmail());
        verify(userRepository).save(any(User.class));
        verify(refreshTokenService).createRefreshToken(any(User.class));
        verify(jwtService).generateToken(any(User.class));
    }

    @Test
    void register_ShouldThrowConflict_WhenEmailAlreadyExists() {
        AuthRequest request = new AuthRequest();
        request.setEmail("existing@test.com");
        request.setPassword("password123");

        User existingUser = new User();
        existingUser.setEmail(request.getEmail());

        when(userRepository.findByEmail(request.getEmail())).thenReturn(Optional.of(existingUser));

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            authService.register(request);
        });

        assertEquals(HttpStatus.CONFLICT, exception.getStatusCode());
        assertEquals("Email already registered", exception.getReason());

        verify(userRepository, never()).save(any(User.class));
        verify(refreshTokenService, never()).createRefreshToken(any());
    }


    @Test
    void login_ShouldReturnAuthResponse_WhenCredentialsAreValid() {
        AuthRequest request = new AuthRequest();
        request.setEmail("user@test.com");
        request.setPassword("password123");

        String accessToken = "jwt.access.token";
        UUID refreshTokenUuid = UUID.randomUUID();

        User user = new User();
        user.setId(1L);
        user.setEmail(request.getEmail());
        user.setRole(User.Role.USER);

        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setToken(refreshTokenUuid);
        refreshToken.setUser(user);

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(authentication);
        when(userRepository.findByEmail(request.getEmail())).thenReturn(Optional.of(user));
        when(refreshTokenService.createRefreshToken(user)).thenReturn(refreshToken);
        when(jwtService.generateToken(user)).thenReturn(accessToken);

        AuthResponse response = authService.login(request);

        assertNotNull(response);
        assertEquals(accessToken, response.getAccessToken());
        assertEquals(refreshTokenUuid.toString(), response.getRefreshToken());

        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(userRepository).findByEmail(request.getEmail());
        verify(refreshTokenService).createRefreshToken(user);
        verify(jwtService).generateToken(user);
    }

    @Test
    void login_ShouldThrowUnauthorized_WhenAuthenticationFails() {
        AuthRequest request = new AuthRequest();
        request.setEmail("user@test.com");
        request.setPassword("wrongPassword");

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new RuntimeException("Bad credentials"));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            authService.login(request);
        });

        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(userRepository, never()).findByEmail(any());
        verify(refreshTokenService, never()).createRefreshToken(any());
    }

    @Test
    void login_ShouldThrowUnauthorized_WhenUserNotFound() {
        AuthRequest request = new AuthRequest();
        request.setEmail("notfound@test.com");
        request.setPassword("password123");

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(authentication);
        when(userRepository.findByEmail(request.getEmail())).thenReturn(Optional.empty());

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            authService.login(request);
        });

        assertEquals(HttpStatus.UNAUTHORIZED, exception.getStatusCode());
        assertEquals("Invalid credentials", exception.getReason());
    }


    @Test
    void refresh_ShouldReturnNewAuthResponse_WhenTokenIsValid() {
        UUID refreshTokenUuid = UUID.randomUUID();
        RefreshTokenRequest request = new RefreshTokenRequest();
        request.setRefreshToken(refreshTokenUuid);

        String newAccessToken = "new.jwt.access.token";

        User user = new User();
        user.setId(1L);
        user.setEmail("user@test.com");

        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setToken(refreshTokenUuid);
        refreshToken.setUser(user);

        when(refreshTokenService.findByToken(refreshTokenUuid)).thenReturn(Optional.of(refreshToken));

        when(jwtService.generateToken(user)).thenReturn(newAccessToken);

        AuthResponse response = authService.refresh(request);

        assertNotNull(response);
        assertEquals(newAccessToken, response.getAccessToken());
        assertEquals(refreshTokenUuid.toString(), response.getRefreshToken());

        verify(refreshTokenService).findByToken(refreshTokenUuid);
        verify(refreshTokenService).verifyExpiration(refreshToken);
        verify(jwtService).generateToken(user);
    }

    @Test
    void refresh_ShouldThrowException_WhenRefreshTokenNotFound() {
        UUID refreshTokenUuid = UUID.randomUUID();
        RefreshTokenRequest request = new RefreshTokenRequest();
        request.setRefreshToken(refreshTokenUuid);

        when(refreshTokenService.findByToken(refreshTokenUuid)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            authService.refresh(request);
        });

        assertEquals("Invalid refresh token", exception.getMessage());
        verify(refreshTokenService).findByToken(refreshTokenUuid);
        verify(refreshTokenService, never()).verifyExpiration(any());
    }


    @Test
    void logout_ShouldDeleteRefreshToken() {
        User user = new User();
        user.setId(1L);
        user.setEmail("user@test.com");

        authService.logout(user);

        verify(refreshTokenService).deleteByUserId(user.getId());
    }
}