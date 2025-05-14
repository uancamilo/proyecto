package com.proyecto.integrador.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

@Schema(description = "Información de una invitación")
public class InvitacionResponse {

    private Long id;
    private String proyectoNombre;
    private String emailInvitado;
    private String invitadoPor;
    private String estado;
    private LocalDateTime fechaInvitacion;

    public InvitacionResponse() {}

    public InvitacionResponse(Long id, String proyectoNombre, String emailInvitado, String invitadoPor, String estado, LocalDateTime fechaInvitacion) {
        this.id = id;
        this.proyectoNombre = proyectoNombre;
        this.emailInvitado = emailInvitado;
        this.invitadoPor = invitadoPor;
        this.estado = estado;
        this.fechaInvitacion = fechaInvitacion;
    }

    // Getters y setters...

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getProyectoNombre() { return proyectoNombre; }
    public void setProyectoNombre(String proyectoNombre) { this.proyectoNombre = proyectoNombre; }

    public String getEmailInvitado() { return emailInvitado; }
    public void setEmailInvitado(String emailInvitado) { this.emailInvitado = emailInvitado; }

    public String getInvitadoPor() { return invitadoPor; }
    public void setInvitadoPor(String invitadoPor) { this.invitadoPor = invitadoPor; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    public LocalDateTime getFechaInvitacion() { return fechaInvitacion; }
    public void setFechaInvitacion(LocalDateTime fechaInvitacion) { this.fechaInvitacion = fechaInvitacion; }
}
