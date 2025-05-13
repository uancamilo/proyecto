package com.proyecto.integrador.controller;

import com.proyecto.integrador.dto.AuthRequest;
import com.proyecto.integrador.service.JwtService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.*;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/auth")
@Tag(name = "Autenticación", description = "Operaciones relacionadas con login")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public AuthController(AuthenticationManager authenticationManager, JwtService jwtService) {
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }

    @Operation(
            summary = "Iniciar sesión",
            description = "Autentica al usuario y devuelve una cookie JWT HttpOnly en la respuesta.",
            requestBody = @RequestBody(
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = AuthRequest.class)
                    )
            ),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Login exitoso. Cookie JWT enviada",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(
                                            example = "{\"message\": \"Login exitoso\"}"
                                    )
                            )
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "Credenciales inválidas",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(
                                            example = "{\"error\": \"Credenciales inválidas\"}"
                                    )
                            )
                    )
            }
    )

    @PostMapping("/login")
    public ResponseEntity<?> login(@org.springframework.web.bind.annotation.RequestBody AuthRequest authRequest) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authRequest.getEmail(), authRequest.getPassword())
            );

            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            String token = jwtService.generateToken(userDetails);

            ResponseCookie jwtCookie = ResponseCookie.from("jwt", token)
                    .httpOnly(true)
                    .secure(true)
                    .path("/")
                    .maxAge(60 * 60)
                    .sameSite("None")
                    .build();

            return ResponseEntity.ok()
                    .header(HttpHeaders.SET_COOKIE, jwtCookie.toString())
                    .body(Map.of("message", "Login exitoso"));

        } catch (AuthenticationException e) {
            return ResponseEntity.status(401).body(Map.of("error", "Credenciales inválidas"));
        }
    }
}
