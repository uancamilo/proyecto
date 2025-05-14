package com.proyecto.integrador.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "invitacion")
public class Invitacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "proyecto_id")
    private Proyecto proyecto;

    @Column(name = "invitado_email", nullable = false)
    private String invitadoEmail;

    @ManyToOne(optional = false)
    @JoinColumn(name = "invitador_id")
    private Usuario invitador;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoInvitacion estado;

    @Column(name = "fecha_invitacion", nullable = false)
    private LocalDateTime fechaInvitacion;

    public Invitacion() {}

    public Invitacion(Proyecto proyecto, String invitadoEmail, Usuario invitador, EstadoInvitacion estado, LocalDateTime fechaInvitacion) {
        this.proyecto = proyecto;
        this.invitadoEmail = invitadoEmail;
        this.invitador = invitador;
        this.estado = estado;
        this.fechaInvitacion = fechaInvitacion;
    }

    public Long getId() { return id; }

    public Proyecto getProyecto() { return proyecto; }

    public void setProyecto(Proyecto proyecto) { this.proyecto = proyecto; }

    public String getInvitadoEmail() { return invitadoEmail; }

    public void setInvitadoEmail(String invitadoEmail) { this.invitadoEmail = invitadoEmail; }

    public Usuario getInvitador() { return invitador; }

    public void setInvitador(Usuario invitador) { this.invitador = invitador; }

    public EstadoInvitacion getEstado() { return estado; }

    public void setEstado(EstadoInvitacion estado) { this.estado = estado; }

    public LocalDateTime getFechaInvitacion() { return fechaInvitacion; }

    public void setFechaInvitacion(LocalDateTime fechaInvitacion) { this.fechaInvitacion = fechaInvitacion; }
}
