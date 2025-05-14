package com.proyecto.integrador.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.time.LocalDate;

@Schema(description = "Datos para crear o actualizar un proyecto")
public class ProyectoRequest {

    @Schema(description = "Nombre del proyecto", example = "Sistema de Gestión Escolar")
    private String nombre;

    @Schema(description = "Descripción detallada del proyecto", example = "Aplicación web para gestionar notas y estudiantes")
    private String descripcion;

    @Schema(description = "Estado del proyecto", example = "PUBLICADO")
    private String estado;

    @Schema(description = "Fecha límite para postularse", example = "2025-07-01")
    private LocalDate fechaPostulacion;

    @Schema(description = "Fecha de entrega del proyecto", example = "2025-09-30")
    private LocalDate fechaEntrega;

    @Schema(description = "Valor económico del proyecto", example = "5000.00")
    private BigDecimal valorMonetario;

    public ProyectoRequest() {}

    public String getNombre() { return nombre; }

    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getDescripcion() { return descripcion; }

    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public String getEstado() { return estado; }

    public void setEstado(String estado) { this.estado = estado; }

    public LocalDate getFechaPostulacion() { return fechaPostulacion; }

    public void setFechaPostulacion(LocalDate fechaPostulacion) { this.fechaPostulacion = fechaPostulacion; }

    public LocalDate getFechaEntrega() { return fechaEntrega; }

    public void setFechaEntrega(LocalDate fechaEntrega) { this.fechaEntrega = fechaEntrega; }

    public BigDecimal getValorMonetario() { return valorMonetario; }

    public void setValorMonetario(BigDecimal valorMonetario) { this.valorMonetario = valorMonetario; }
}
