package com.victor.usersms.controller;

import com.victor.usersms.dto.StandardErrorDto;
import com.victor.usersms.dto.UserRequestDto;
import com.victor.usersms.dto.UserResponseDto;
import com.victor.usersms.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
@Tag(name = "Users", description = "Endpoints to management of users")
@ApiResponses
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }
    @Operation(summary = "Save new user", description = "Create a new user on Systems with profile basic.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "User created with success"),
            @ApiResponse(responseCode = "409", description = "Conflict data like email or username already exists",
                    content = @Content(schema = @Schema(implementation = StandardErrorDto.class))),
    })

    @PostMapping("/save")
    public ResponseEntity<UserResponseDto> save(@Valid @RequestBody UserRequestDto dto){
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.save(dto));
    }

    @Operation(summary = "List all users", description = "Allow only profile of ADMIN.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List returned with success"),
            @ApiResponse(responseCode = "401", description = "Not authorized (Token unpresent or invalid)"),
            @ApiResponse(responseCode = "403", description = "Forbidden - access denied. Only profile of ADMIN can access this data")
    })
    @GetMapping("/show-all")
    public ResponseEntity<List<UserResponseDto>> findAll(){
        return ResponseEntity.ok(userService.findAll());
    }

    @Operation(summary = "List a user by id", description = "List user according to their corresponding ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User found"),
            @ApiResponse(responseCode = "404", description = "User not found",
                    content = @Content(schema = @Schema(implementation = StandardErrorDto.class)))
    })
    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDto> findById(@PathVariable ("id") Integer id){
        return ResponseEntity.ok(userService.findById(id));
    }

    @Operation(summary = "Update a user by id", description = "Update data of user according to their corresponding ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User updated with success"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    @PutMapping("/{id}")
    public ResponseEntity<UserResponseDto> updateUser(@PathVariable ("id") Integer id,
    @Valid @RequestBody UserRequestDto dto){
        return ResponseEntity.ok(userService.updateUser(id,dto));
    }

    @Operation(summary = "Delete a user by id", description = "Delete user based on corresponding ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "User deleted with success - So no Content"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable ("id") Integer id){
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
}