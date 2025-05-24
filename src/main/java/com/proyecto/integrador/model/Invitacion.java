package com.proyecto.integrador.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "invitacion")
public class Invitacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "El proyecto es obligatorio")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "proyecto_id", nullable = false)
    private Proyecto proyecto;

    @NotBlank(message = "El email del invitado es obligatorio")
    @Email(message = "Debe ser un email válido")
    @Column(name = "invitado_email", nullable = false)
    private String invitadoEmail;

    @NotNull(message = "El usuario que invita es obligatorio")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "invitador_id", nullable = false)
    private Usuario invitador;

    @NotNull(message = "El estado es obligatorio")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoInvitacion estado = EstadoInvitacion.PENDIENTE;

    @CreationTimestamp
    @Column(name = "fecha_invitacion", nullable = false, updatable = false)
    private LocalDateTime fechaInvitacion;

    // Constructor vacío requerido por JPA
    public Invitacion() {}

    public Invitacion(Proyecto proyecto, String invitadoEmail, Usuario invitador, EstadoInvitacion estado) {
        this.proyecto = proyecto;
        this.invitadoEmail = invitadoEmail;
        this.invitador = invitador;
        this.estado = estado;
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
    public void setFechaInvitacion(LocalDateTime fechaInvitacion) {
        this.fechaInvitacion = fechaInvitacion;
    }

}
