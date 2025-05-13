package com.proyecto.integrador.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/admin")
@Tag(name = "Administrador", description = "Endpoints exclusivos para usuarios con rol ADMIN")
public class AdminController {

    @Operation(
            summary = "Endpoint protegido para administradores",
            description = "Este endpoint solo puede ser accedido por usuarios con rol ADMIN",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Acceso permitido para ADMIN"),
                    @ApiResponse(responseCode = "403", description = "Acceso denegado")
            }
    )
    @GetMapping("/privado")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> privadoAdmin() {
        return ResponseEntity.ok(Map.of("message", "Acceso concedido solo para administradores"));
    }
}
