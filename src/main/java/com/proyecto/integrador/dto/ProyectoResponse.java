package com.proyecto.integrador.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.time.LocalDate;

@Schema(description = "Datos detallados de un proyecto")
public class ProyectoResponse {

    @Schema(description = "ID del proyecto", example = "1")
    private Long id;

    @Schema(description = "Nombre del proyecto", example = "Sistema de Gestión Escolar")
    private String nombre;

    @Schema(description = "Descripción detallada", example = "Aplicación web para gestionar notas y estudiantes")
    private String descripcion;

    @Schema(description = "Estado actual del proyecto", example = "PUBLICADO")
    private String estado;

    @Schema(description = "Fecha de publicación", example = "2025-06-01")
    private LocalDate fechaPublicacion;

    @Schema(description = "Fecha límite para postularse", example = "2025-07-01")
    private LocalDate fechaPostulacion;

    @Schema(description = "Fecha de entrega del proyecto", example = "2025-09-30")
    private LocalDate fechaEntrega;

    @Schema(description = "Valor económico del proyecto", example = "5000.00")
    private BigDecimal valorMonetario;

    @Schema(description = "Email del usuario que creó el proyecto", example = "admin@email.com")
    private String creadoPor;

    public ProyectoResponse() {}

    public ProyectoResponse(Long id, String nombre, String descripcion, String estado,
                            LocalDate fechaPublicacion, LocalDate fechaPostulacion,
                            LocalDate fechaEntrega, BigDecimal valorMonetario, String creadoPor) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.estado = estado;
        this.fechaPublicacion = fechaPublicacion;
        this.fechaPostulacion = fechaPostulacion;
        this.fechaEntrega = fechaEntrega;
        this.valorMonetario = valorMonetario;
        this.creadoPor = creadoPor;
    }

    public Long getId() { return id; }

    public String getNombre() { return nombre; }

    public String getDescripcion() { return descripcion; }

    public String getEstado() { return estado; }

    public LocalDate getFechaPublicacion() { return fechaPublicacion; }

    public LocalDate getFechaPostulacion() { return fechaPostulacion; }

    public LocalDate getFechaEntrega() { return fechaEntrega; }

    public BigDecimal getValorMonetario() { return valorMonetario; }

    public String getCreadoPor() { return creadoPor; }

    public void setId(Long id) { this.id = id; }

    public void setNombre(String nombre) { this.nombre = nombre; }

    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public void setEstado(String estado) { this.estado = estado; }

    public void setFechaPublicacion(LocalDate fechaPublicacion) { this.fechaPublicacion = fechaPublicacion; }

    public void setFechaPostulacion(LocalDate fechaPostulacion) { this.fechaPostulacion = fechaPostulacion; }

    public void setFechaEntrega(LocalDate fechaEntrega) { this.fechaEntrega = fechaEntrega; }

    public void setValorMonetario(BigDecimal valorMonetario) { this.valorMonetario = valorMonetario; }

    public void setCreadoPor(String creadoPor) { this.creadoPor = creadoPor; }
}
