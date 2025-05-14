package com.proyecto.integrador.mapper;

import com.proyecto.integrador.dto.ProyectoRequest;
import com.proyecto.integrador.dto.ProyectoResponse;
import com.proyecto.integrador.model.EstadoProyecto;
import com.proyecto.integrador.model.Proyecto;
import com.proyecto.integrador.model.Usuario;

public class ProyectoMapper {

    public static Proyecto toEntity(ProyectoRequest dto, Usuario creador) {
        Proyecto proyecto = new Proyecto();
        updateEntity(proyecto, dto); // reutilizamos la lógica de mapeo
        proyecto.setCreadoPor(creador);
        return proyecto;
    }


    public static void updateEntity(Proyecto proyecto, ProyectoRequest dto) {
        proyecto.setNombre(dto.getNombre());
        proyecto.setDescripcion(dto.getDescripcion());
        proyecto.setEstado(EstadoProyecto.valueOf(dto.getEstado()));
        proyecto.setFechaPostulacion(dto.getFechaPostulacion());
        proyecto.setFechaEntrega(dto.getFechaEntrega());
        proyecto.setValorMonetario(dto.getValorMonetario());
    }


    public static ProyectoResponse toResponse(Proyecto proyecto) {
        return new ProyectoResponse(
                proyecto.getId(),
                proyecto.getNombre(),
                proyecto.getDescripcion(),
                proyecto.getEstado().name(),
                proyecto.getFechaPublicacion(),
                proyecto.getFechaPostulacion(),
                proyecto.getFechaEntrega(),
                proyecto.getValorMonetario(),
                proyecto.getCreadoPor().getEmail()
        );
    }
}
