package com.proyecto.integrador.controller;

import com.proyecto.integrador.dto.AuthRequest;
import com.proyecto.integrador.service.JwtService;
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
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public AuthController(AuthenticationManager authenticationManager, JwtService jwtService) {
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthRequest authRequest) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authRequest.getEmail(), authRequest.getPassword())
            );

            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            String token = jwtService.generateToken(userDetails);

            // Crear cookie HttpOnly con el token
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
