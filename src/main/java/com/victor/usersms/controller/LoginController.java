package com.victor.usersms.controller;

import com.victor.usersms.config.ApiNotFoundResponse;
import com.victor.usersms.dto.LoginRequestDto;
import com.victor.usersms.dto.LoginResponseDto;
import com.victor.usersms.dto.StandardErrorDto;
import com.victor.usersms.service.LoginService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name="Login", description = "Login for registered users")
public class LoginController {
    private final LoginService loginService;

    public LoginController(LoginService loginService) {
        this.loginService = loginService;
    }

    @Operation(summary = "Users log in.", description = "Users authenticate and receive an access token unique")
    @ApiNotFoundResponse
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Login realized with success. Token generated."),
            @ApiResponse(responseCode = "401", description = "Bad credentials. Username or password invalid.",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = StandardErrorDto.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error.",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = StandardErrorDto.class)))
    })
    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login (@RequestBody @Valid LoginRequestDto dto){
        var login = loginService.login(dto);
        return ResponseEntity.ok(login);
    }
}
