package com.proyecto.integrador.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "proyecto")
public class Proyecto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "El nombre es obligatorio")
    @Column(nullable = false)
    private String nombre;

    @Size(max = 10000, message = "La descripción es demasiado larga")
    @Column(columnDefinition = "TEXT")
    private String descripcion;

    @NotNull(message = "El estado es obligatorio")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoProyecto estado = EstadoProyecto.EN_CURSO;

    @Column(name = "fecha_publicacion")
    private LocalDate fechaPublicacion;

    @Column(name = "fecha_postulacion")
    private LocalDate fechaPostulacion;

    @Column(name = "fecha_entrega")
    private LocalDate fechaEntrega;

    @PositiveOrZero(message = "El valor monetario no puede ser negativo")
    @Column(name = "valor_monetario", precision = 12, scale = 2)
    private BigDecimal valorMonetario;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "creado_por_id", nullable = false)
    @NotNull(message = "El creador del proyecto es obligatorio")
    private Usuario creadoPor;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    // Constructor vacío requerido por JPA
    public Proyecto() {}

    // Getters y Setters
    public Long getId() { return id; }

    public void setId(Long id) { this.id = id; }

    public String getNombre() { return nombre; }

    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getDescripcion() { return descripcion; }

    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public EstadoProyecto getEstado() { return estado; }

    public void setEstado(EstadoProyecto estado) { this.estado = estado; }

    public LocalDate getFechaPublicacion() { return fechaPublicacion; }

    public void setFechaPublicacion(LocalDate fechaPublicacion) { this.fechaPublicacion = fechaPublicacion; }

    public LocalDate getFechaPostulacion() { return fechaPostulacion; }

    public void setFechaPostulacion(LocalDate fechaPostulacion) { this.fechaPostulacion = fechaPostulacion; }

    public LocalDate getFechaEntrega() { return fechaEntrega; }

    public void setFechaEntrega(LocalDate fechaEntrega) { this.fechaEntrega = fechaEntrega; }

    public BigDecimal getValorMonetario() { return valorMonetario; }

    public void setValorMonetario(BigDecimal valorMonetario) { this.valorMonetario = valorMonetario; }

    public Usuario getCreadoPor() { return creadoPor; }

    public void setCreadoPor(Usuario creadoPor) { this.creadoPor = creadoPor; }

    public LocalDateTime getCreatedAt() { return createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
}
