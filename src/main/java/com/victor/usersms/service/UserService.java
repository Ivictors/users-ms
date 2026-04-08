package com.victor.usersms.service;


import com.victor.usersms.dto.UserRequestDto;
import com.victor.usersms.dto.UserResponseDto;
import com.victor.usersms.enums.UserStatus;
import com.victor.usersms.exceptions.EmailException;
import com.victor.usersms.exceptions.UserNotFoundException;
import com.victor.usersms.model.Role;
import com.victor.usersms.model.UserModel;
import com.victor.usersms.repository.RoleRepository;
import com.victor.usersms.repository.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;



@Service
public class UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, RoleRepository roleRepository, BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public UserResponseDto save(UserRequestDto dto) {

        if (userRepository.existsByEmail(dto.email())) {
            throw new EmailException("Email already exists.");
        }
        Role role = roleRepository.findByName(Role.Values.BASIC.name());

        UserModel user = new UserModel();
        user.setEmail(dto.email());
        user.setUsername(dto.username());
        user.setPassword(passwordEncoder.encode(dto.password()));
        user.setUserStatus(UserStatus.ACTIVE);
        user.setRole(Set.of(role));

        var savedUser = userRepository.save(user);

        return getUserResponseDto(savedUser);
    }

    public List<UserResponseDto> findAll() {
        return userRepository.findAll().stream()
                .map(this::getUserResponseDto)
                .toList();
    }

    public UserResponseDto findById(Integer id) {
        var user = userRepository.findById(id).orElseThrow(
                () -> new UserNotFoundException("User not found.")
        );
        return getUserResponseDto(user);
    }

    @Transactional
    public UserResponseDto updateUser(Integer id, UserRequestDto dto) {
        var user = userRepository.findById(id).orElseThrow(
                () -> new UserNotFoundException("User not found.")
        );

        if (!dto.email().equals(user.getEmail())) {
            if (userRepository.existsByEmail(dto.email())) {
                throw new EmailException("Email already exists.");
            }
        }
        user.setEmail(dto.email());
        user.setUsername(dto.username());
        user.setPassword(passwordEncoder.encode(dto.password()));

        var savedUser = userRepository.save(user);

        return getUserResponseDto(savedUser);
    }

    @Transactional
    public void deleteUser(Integer id) {
        var user = userRepository.findById(id).orElseThrow(
                () -> new UserNotFoundException("User not found.")
        );
        user.setUserStatus(UserStatus.INACTIVE);
        userRepository.save(user);
    }

    private UserResponseDto getUserResponseDto(UserModel user) {
        return new UserResponseDto(
                user.getUserId(),
                user.getEmail(),
                user.getUsername()
        );
    }
}