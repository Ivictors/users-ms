package com.victor.usersms.service;

import com.victor.usersms.dto.LoginRequestDto;
import com.victor.usersms.dto.LoginResponseDto;
import com.victor.usersms.exceptions.UserNotFoundException;
import com.victor.usersms.model.Role;
import com.victor.usersms.repository.UserRepository;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.stream.Collectors;

@Service
public class LoginService {

    private final JwtEncoder jwtEncoder;
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public LoginService(JwtEncoder jwtEncoder, UserRepository userRepository, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.jwtEncoder = jwtEncoder;
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    public LoginResponseDto login(LoginRequestDto loginRequestDto) {
        var user = userRepository.findByUsername(loginRequestDto.username())
                .orElseThrow(() -> new UserNotFoundException("user not found"));

        if (!bCryptPasswordEncoder.matches(loginRequestDto.password(), user.getPassword())) {
            throw new BadCredentialsException("username or password is invalid.");
        }

        var now = Instant.now();
        long expiresIn = 360L;

        var scopes = user.getRole()
                .stream()
                .map(Role::getName)
                .collect(Collectors.joining(" "));

        var claims = JwtClaimsSet.builder()
                .issuer("backend")
                .subject(user.getUserId().toString())
                .issuedAt(now)
                .expiresAt(now.plusSeconds(expiresIn))
                .claim("scope", scopes)
                .build();

        var jwtValue = jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();

        return new LoginResponseDto(jwtValue, expiresIn);
    }
}