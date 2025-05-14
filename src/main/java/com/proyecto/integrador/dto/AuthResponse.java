package com.proyecto.integrador.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Respuesta después de iniciar sesión exitosamente")
public class AuthResponse {

    @Schema(description = "Mensaje de éxito", example = "Operación exitosa")
    private String message;

    @Schema(description = "Datos del usuario autenticado")
    private UsuarioResponse user;

    public AuthResponse() {}

    public AuthResponse(String message) {
        this.message = message;
    }

    public AuthResponse(String message, UsuarioResponse user) {
        this.message = message;
        this.user = user;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public UsuarioResponse getUser() {
        return user;
    }

    public void setUser(UsuarioResponse user) {
        this.user = user;
    }
}
