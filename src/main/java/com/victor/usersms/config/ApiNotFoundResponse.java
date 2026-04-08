package com.victor.usersms.config;

import com.victor.usersms.dto.StandardErrorDto;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@ApiResponses({
   @ApiResponse(responseCode = "404", description = "User not found",
            content = @Content(mediaType = "application/json",schema = @Schema(implementation = StandardErrorDto.class)))
})
public @interface ApiNotFoundResponse {
}
