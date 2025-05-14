package com.proyecto.integrador.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Datos para invitar a un usuario a un proyecto")
public class InvitacionRequest {

    @Schema(description = "ID del proyecto", example = "1")
    private Long proyectoId;

    @Schema(description = "Correo electrónico del invitado", example = "usuario@email.com")
    private String email;

    public Long getProyectoId() {
        return proyectoId;
    }

    public void setProyectoId(Long proyectoId) {
        this.proyectoId = proyectoId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
