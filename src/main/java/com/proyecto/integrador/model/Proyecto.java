package com.proyecto.integrador.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "proyecto")
public class Proyecto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nombre;

    @Column(columnDefinition = "TEXT")
    private String descripcion;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoProyecto estado;

    @Column(name = "fecha_publicacion")
    private LocalDate fechaPublicacion;

    @Column(name = "fecha_postulacion")
    private LocalDate fechaPostulacion;

    @Column(name = "fecha_entrega")
    private LocalDate fechaEntrega;

    @Column(name = "valor_monetario", precision = 12, scale = 2)
    private BigDecimal valorMonetario;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "creado_por_id", nullable = false)
    private Usuario creadoPor;

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
}
