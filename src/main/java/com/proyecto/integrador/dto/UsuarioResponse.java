package com.proyecto.integrador.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Datos del usuario autenticado")
public class UsuarioResponse {

    @Schema(description = "Nombre del usuario", example = "Juan Camilo")
    private String nombre;

    @Schema(description = "Correo electrónico del usuario", example = "juan@email.com")
    private String email;

    @Schema(description = "Rol del usuario", example = "ROLE_USER")
    private String rol;

    public UsuarioResponse() {}

    public UsuarioResponse(String nombre, String email, String rol) {
        this.nombre = nombre;
        this.email = email;
        this.rol = rol;
    }

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

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }
}
