package com.proyecto.integrador.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/user")
@Tag(name = "Usuario", description = "Endpoints accesibles para usuarios autenticados con rol USER o ADMIN")
public class UserController {

    @Operation(
            summary = "Perfil del usuario",
            description = "Devuelve la información básica del perfil. Acceso solo para usuarios autenticados con rol USER o ADMIN.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Acceso permitido al perfil"),
                    @ApiResponse(responseCode = "403", description = "Acceso denegado")
            }
    )
    @GetMapping("/perfil")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<?> verPerfil() {
        return ResponseEntity.ok(Map.of("message", "Este es tu perfil de usuario."));
    }
}
