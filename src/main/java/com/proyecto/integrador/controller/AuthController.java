package com.proyecto.integrador.controller;

import com.proyecto.integrador.dto.*;
import com.proyecto.integrador.model.UsuarioUser;
import com.proyecto.integrador.repository.UsuarioRepository;
import com.proyecto.integrador.security.CustomUserDetails;
import com.proyecto.integrador.security.JwtCookieUtil;
import com.proyecto.integrador.service.JwtService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.web.bind.annotation.RequestBody;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import com.proyecto.integrador.model.Usuario;

import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Value("${app.env.production:false}")
    private boolean isProduction;

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthController(
            AuthenticationManager authenticationManager,
            JwtService jwtService,
            UsuarioRepository usuarioRepository,
            PasswordEncoder passwordEncoder
    ) {
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Operation(
            summary = "Iniciar sesión",
            description = "Autentica al usuario y devuelve una cookie JWT HttpOnly.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Login exitoso",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = AuthResponse.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "Credenciales inválidas",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(example = "{\"error\": \"Credenciales inválidas\"}")
                            )
                    )
            }
    )

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody AuthRequest authRequest) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            authRequest.getEmail(),
                            authRequest.getPassword()
                    )
            );

            CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();

            String token = jwtService.generateToken(userDetails);

            ResponseCookie jwtCookie = ResponseCookie.from("jwt", token)
                    .httpOnly(true)
                    .secure(isProduction)
                    .path("/")
                    .maxAge(60 * 60)
                    .sameSite(isProduction ? "None" : "Lax")
                    .build();

            UsuarioResponse usuarioResponse = new UsuarioResponse(
                    userDetails.getNombre(),
                    userDetails.getEmail(),
                    "ROLE_" + userDetails.getRol()
            );

            return ResponseEntity.ok()
                    .header(HttpHeaders.SET_COOKIE, jwtCookie.toString())
                    .body(new AuthResponse("Login exitoso", usuarioResponse));

        } catch (AuthenticationException e) {
            return ResponseEntity.status(401)
                    .body(new AuthResponse("Credenciales inválidas"));
        }
    }

    @Operation(
            summary = "Registrar nuevo usuario",
            description = "Crea un nuevo usuario con rol USER por defecto",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = RegisterRequest.class)
                    )
            ),
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "Usuario registrado exitosamente",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = RegisterResponse.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Email ya registrado",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(example = "{\"error\": \"El email ya está en uso\"}")
                            )
                    )
            }
    )

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
        if (usuarioRepository.existsByEmail(request.getEmail())) {
            return ResponseEntity.badRequest().body(Map.of("error", "El email ya está en uso"));
        }

        UsuarioUser nuevo = new UsuarioUser();
        nuevo.setNombre(request.getNombre());
        nuevo.setEmail(request.getEmail());
        nuevo.setPassword(passwordEncoder.encode(request.getPassword()));

        usuarioRepository.save(nuevo);

        return ResponseEntity
                .status(201)
                .body(new RegisterResponse("Usuario registrado correctamente"));
    }

    @Operation(
            summary = "Cerrar sesión del usuario",
            description = "Elimina la cookie JWT del navegador para cerrar sesión.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Sesión cerrada correctamente",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = AuthResponse.class)
                            )
                    )
            }
    )
    @PostMapping("/logout")
    public ResponseEntity<AuthResponse> logout(HttpServletResponse response) {
        JwtCookieUtil.clearJwtCookie(response, isProduction);
        return ResponseEntity.ok(new AuthResponse("Sesión cerrada correctamente"));
    }

    @Operation(
            summary = "Obtener información del usuario autenticado",
            description = "Devuelve los datos del usuario autenticado basándose en el token JWT",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Datos del usuario autenticado",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = UsuarioResponse.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "No autenticado",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(example = "{\"error\": \"No autenticado\"}")
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Usuario no encontrado en base de datos",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(example = "{\"error\": \"Usuario no encontrado\"}")
                            )
                    )
            }
    )
    @GetMapping("/me")
    public ResponseEntity<?> me(@AuthenticationPrincipal UserDetails userDetails) {
        if (userDetails == null) {
            return ResponseEntity.status(401).body(Map.of("error", "No autenticado"));
        }

        Usuario usuario = usuarioRepository.findByEmail(userDetails.getUsername()).orElse(null);
        if (usuario == null) {
            return ResponseEntity.status(404).body(Map.of("error", "Usuario no encontrado"));
        }

        UsuarioResponse response = new UsuarioResponse(
                usuario.getNombre(),
                usuario.getEmail(),
                usuario.getRol()
        );

        return ResponseEntity.ok(response);
    }

}
