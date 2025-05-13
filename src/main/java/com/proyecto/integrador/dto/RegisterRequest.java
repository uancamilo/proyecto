package com.proyecto.integrador.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Datos necesarios para registrar un nuevo usuario")
public class RegisterRequest {

    @Schema(example = "Juan Pérez", description = "Nombre completo del usuario")
    private String nombre;

    @Schema(example = "juan@email.com", description = "Correo electrónico único")
    private String email;

    @Schema(example = "claveSegura123", description = "Contraseña sin encriptar")
    private String password;

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

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
