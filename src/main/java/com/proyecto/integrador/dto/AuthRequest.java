package com.proyecto.integrador.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Datos para autenticación de usuario")
public class AuthRequest {

    @Schema(example = "usuario@email.com", description = "Correo electrónico del usuario")
    private String email;

    @Schema(example = "claveSegura123", description = "Contraseña del usuario")
    private String password;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
