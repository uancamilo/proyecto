package com.proyecto.integrador.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Respuesta después de iniciar sesión exitosamente")
public class AuthResponse {

    @Schema(description = "Mensaje de éxito", example = "Operación exitosa")
    private String message;

    public AuthResponse() {
    }

    public AuthResponse(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
