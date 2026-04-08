package com.victor.usersms.service;

import com.victor.usersms.dto.LoginRequestDto;
import com.victor.usersms.exceptions.UserNotFoundException;
import com.victor.usersms.model.Role;
import com.victor.usersms.model.UserModel;
import com.victor.usersms.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;

import java.util.Optional;
import java.util.Set;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class LoginServiceTest {
    @Mock
    private JwtEncoder jwtEncoder;
    @Mock
    private UserRepository userRepository;
    @Mock
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @InjectMocks
    private LoginService loginService;

    private LoginRequestDto buildLoginRequest() {
        return new LoginRequestDto("victor", "victor10");
    }

    private UserModel buildUserModel() {

        var role = new Role();
        role.setName("ADMIN");

        var user = new UserModel();
        user.setUserId(1);
        user.setUsername("victor");
        user.setRole(Set.of(role));
        return user;
    }

    @Nested
    class login {

        @Test
        @DisplayName("should throw exception when user is not found in database.")
        void shouldThrowExceptionUserNotFound() {
            var loginRequest = buildLoginRequest();
            when(userRepository.findByUsername(loginRequest.username())).thenReturn(Optional.empty());

            assertThrows(UserNotFoundException.class, () -> loginService.login(loginRequest));

            verify(jwtEncoder, never()).encode(any());
        }

        @Test
        @DisplayName("should throw exception when credentials is wrong")
        void shouldThrowExceptionBadCredentials(){
            var loginRequest = buildLoginRequest();
            var userSpy = spy(buildUserModel());

            when(userRepository.findByUsername(loginRequest.username())).thenReturn(Optional.of(userSpy));
            when(bCryptPasswordEncoder.matches(buildLoginRequest().password(), userSpy.getPassword())).thenReturn(false);

            assertThrows(BadCredentialsException.class, () -> loginService.login(loginRequest));
            verify(jwtEncoder, never()).encode(any());
        }

        @Test
        @DisplayName("Should return JWT token when login is successful")
        void shouldReturnTokenWhenLoginIsSuccessful() {
            var loginRequest = buildLoginRequest();
            var userSpy = spy(buildUserModel());

            when(userRepository.findByUsername(loginRequest.username())).thenReturn(Optional.of(userSpy));
            when(bCryptPasswordEncoder.matches(buildLoginRequest().password(), userSpy.getPassword())).thenReturn(true);


            Jwt jwtMock = mock(Jwt.class);
            when(jwtMock.getTokenValue()).thenReturn("header.payload.signature");
            when(jwtEncoder.encode(any(JwtEncoderParameters.class))).thenReturn(jwtMock);

            var response = loginService.login(loginRequest);

            assertNotNull(response);
            assertEquals(360L, response.expiresIn());

            verify(jwtEncoder, times(1)).encode(any(JwtEncoderParameters.class));
        }
    }
}