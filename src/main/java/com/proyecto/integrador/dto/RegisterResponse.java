package com.proyecto.integrador.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Respuesta después del registro exitoso de un usuario")
public class RegisterResponse {

    @Schema(description = "Mensaje de confirmación", example = "Usuario registrado correctamente")
    private String message;

    public RegisterResponse() {}

    public RegisterResponse(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
