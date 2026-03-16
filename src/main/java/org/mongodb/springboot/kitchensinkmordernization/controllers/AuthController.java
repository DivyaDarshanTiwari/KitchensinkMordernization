package org.mongodb.springboot.kitchensinkmordernization.controllers;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.mongodb.springboot.kitchensinkmordernization.dto.ApiError;
import org.mongodb.springboot.kitchensinkmordernization.dto.LoginRequestDto;
import org.mongodb.springboot.kitchensinkmordernization.dto.LoginResponseDto;
import org.mongodb.springboot.kitchensinkmordernization.dto.MemberDTO;
import org.mongodb.springboot.kitchensinkmordernization.services.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @Operation(summary = "Used for Login")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User is Authentic",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = MemberDTO.class))}),
            @ApiResponse(responseCode = "401", description = "Users Not authentic",
                    content = {@Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = MemberDTO.class)))}),
    })
    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> logic(@RequestBody @Valid LoginRequestDto loginRequestDto) {
        return ResponseEntity.ok(authService.login(loginRequestDto));
    }

    @Operation(summary = "Used for sigup")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "SignUp Successful",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = MemberDTO.class))}),
            @ApiResponse(responseCode = "409", description = "Duplicate data found",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ApiError.class))}),
    })
    @PostMapping("/signUp")
    public ResponseEntity<Void> createMember(@RequestBody @Valid MemberDTO memberDTO) {
        authService.signUp(memberDTO);
        return ResponseEntity.ok()
                .build();
    }

    @GetMapping("/logout")
    @Operation(summary = "logout user", description = "Log out the user and make the token blacklisted, so that further request cant be made with that token")
    public ResponseEntity<Void> logout(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");

        if (StringUtils.hasText(authHeader) && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            authService.logout(token); // Send token to be blacklisted

            return ResponseEntity.ok().build();
        }

        return ResponseEntity.badRequest().build();
    }

}
