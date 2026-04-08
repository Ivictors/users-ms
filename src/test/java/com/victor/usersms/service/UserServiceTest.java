package com.victor.usersms.service;

import com.victor.usersms.dto.UserRequestDto;
import com.victor.usersms.enums.UserStatus;
import com.victor.usersms.exceptions.EmailException;
import com.victor.usersms.exceptions.UserNotFoundException;
import com.victor.usersms.model.Role;
import com.victor.usersms.model.UserModel;
import com.victor.usersms.repository.RoleRepository;
import com.victor.usersms.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private RoleRepository roleRepository;
    @Mock
    private BCryptPasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    private UserRequestDto buildRequestDto(String email) {
        return new UserRequestDto(email, "victor", "senha123456");
    }

    private UserModel buildUserModel(String email) {
        var user = new UserModel();
        user.setUserId(1);
        user.setEmail(email);
        user.setUsername("victor");
        user.setUserStatus(UserStatus.ACTIVE);
        return user;
    }

    private Role buildRole() {
        var role = new Role();
        role.setRoleId(2);
        role.setName("BASIC");
        return role;
    }

    @Nested
    @DisplayName("Tests for save()")
    class SaveUser {

        @Test
        @DisplayName("Should throw exception when email already exists")
        void shouldThrowExceptionIfEmailExists() {
            var dto = buildRequestDto("victor@gmail.com");

            when(userRepository.existsByEmail(dto.email())).thenReturn(true);

            assertThrows(EmailException.class, () -> userService.save(dto));
            verify(userRepository, never()).save(any());
        }

        @Test
        @DisplayName("Should save a user successfully")
        void shouldSaveUser() {
            var dto = buildRequestDto("victor@gmail.com");
            var userModel = buildUserModel(dto.email());
            var role = buildRole();

            when(userRepository.existsByEmail(dto.email())).thenReturn(false);
            when(roleRepository.findByName(anyString())).thenReturn(role);
            when(passwordEncoder.encode(dto.password())).thenReturn("encodedPassword");
            when(userRepository.save(any(UserModel.class))).thenReturn(userModel);

            var responseDto = userService.save(dto);

            assertNotNull(responseDto);
            assertEquals(dto.email(), responseDto.email());
            verify(userRepository, times(1)).save(any(UserModel.class));
            verify(passwordEncoder, times(1)).encode(dto.password());
        }
    }

    @Nested
    @DisplayName("Tests for findByUser()")
    class FindUser {

        @Test
        @DisplayName("Should throw exception when user not found")
        void shouldThrowExceptionWhenUserNotFound() {
            when(userRepository.findById(1)).thenReturn(Optional.empty());

            assertThrows(UserNotFoundException.class, () -> userService.findById(1));
        }

        @Test
        @DisplayName("Should return user successfully")
        void shouldReturnUser() {
            var user = buildUserModel("victor@hotmail.com");
            when(userRepository.findById(1)).thenReturn(Optional.of(user));

            var response = userService.findById(1);

            assertEquals(user.getEmail(), response.email());
        }
    }

    @Nested
    @DisplayName("Tests for findAll()")
    class FindAllUsers {

        @Test
        @DisplayName("Should return list of UserResponseDto")
        void shouldReturnListOfDtos() {
            var user1 = buildUserModel("victor1@hotmail.com");
            var user2 = buildUserModel("victor2@hotmail.com");

            when(userRepository.findAll()).thenReturn(List.of(user1, user2));

            var response = userService.findAll();

            assertEquals(2, response.size());
            assertEquals("victor1@hotmail.com", response.get(0).email());
            assertEquals("victor2@hotmail.com", response.get(1).email());
        }

        @Test
        @DisplayName("Should return empty list when no users")
        void shouldReturnEmptyList() {
            when(userRepository.findAll()).thenReturn(List.of());

            var response = userService.findAll();

            assertTrue(response.isEmpty());
        }
    }

    @Nested
    @DisplayName("Tests for updateUser()")
    class UpdateUser {

        @Test
        @DisplayName("Should update user successfully")
        void shouldUpdateUserSuccessfully() {
            var dto = buildRequestDto("victor@gmail.com");
            var user = buildUserModel("victor10@hotmail.org");

            when(userRepository.findById(1)).thenReturn(Optional.of(user));
            when(userRepository.existsByEmail(dto.email())).thenReturn(false);
            when(passwordEncoder.encode(dto.password())).thenReturn("encodedPassword");
            when(userRepository.save(any(UserModel.class))).thenReturn(user);

            var response = userService.updateUser(1, dto);

            assertEquals(dto.email(), response.email());
            verify(userRepository).save(user);
            verify(passwordEncoder).encode(dto.password());
        }

        @Test
        @DisplayName("Should throw exception when updating with an already existing email")
        void shouldThrowExceptionWhenUpdatingWithExistingEmail() {
            var dto = buildRequestDto("victor@gmail.com");
            var user = buildUserModel("victor10@hotmail.org");

            when(userRepository.findById(1)).thenReturn(Optional.of(user));
            when(userRepository.existsByEmail(dto.email())).thenReturn(true);

            assertThrows(EmailException.class, () -> userService.updateUser(1, dto));
            verify(userRepository, never()).save(any());
        }

        @Test
        @DisplayName("Should allow update with same email")
        void shouldAllowUpdateWithSameEmail() {
            var dto = buildRequestDto("victor@gmail.com");
            var user = buildUserModel("victor@gmail.com");

            when(userRepository.findById(1)).thenReturn(Optional.of(user));
            when(passwordEncoder.encode(dto.password())).thenReturn("encodedPassword");
            when(userRepository.save(any(UserModel.class))).thenReturn(user);

            var response = userService.updateUser(1, dto);

            assertNotNull(response);
            verify(userRepository).save(user);
        }

        @Test
        @DisplayName("Should throw exception when user not found")
        void shouldThrowExceptionWhenUserNotFound() {
            var dto = buildRequestDto("victor@gmail.com");

            when(userRepository.findById(1)).thenReturn(Optional.empty());

            assertThrows(UserNotFoundException.class, () -> userService.updateUser(1, dto));
        }
    }

    @Nested
    @DisplayName("Tests for deleteUser()")
    class DeleteUser {

        @Test
        @DisplayName("Should deactivate user successfully")
        void shouldDeactivateUser() {
            var user = buildUserModel("victor@gmail.com");
            when(userRepository.findById(1)).thenReturn(Optional.of(user));
            when(userRepository.save(user)).thenReturn(user);

            userService.deleteUser(1);

            assertEquals(UserStatus.INACTIVE, user.getUserStatus());
            verify(userRepository).save(user);
        }

        @Test
        @DisplayName("Should throw exception when user not found on delete")
        void shouldThrowExceptionWhenUserNotFound() {
            when(userRepository.findById(1)).thenReturn(Optional.empty());

            assertThrows(UserNotFoundException.class, () -> userService.deleteUser(1));
        }
    }
}
