package com.proyecto.integrador.service;

import com.proyecto.integrador.dto.ProyectoRequest;
import com.proyecto.integrador.mapper.ProyectoMapper;
import com.proyecto.integrador.model.EstadoProyecto;
import com.proyecto.integrador.model.Proyecto;
import com.proyecto.integrador.repository.ProyectoRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
public class ProyectoService {

    private static final Logger logger = LoggerFactory.getLogger(ProyectoService.class);

    private final ProyectoRepository proyectoRepository;

    public ProyectoService(ProyectoRepository proyectoRepository) {
        this.proyectoRepository = proyectoRepository;
    }

    public Proyecto crearProyecto(Proyecto proyecto) {
        return proyectoRepository.save(proyecto);
    }

    @Transactional(readOnly = true)
    public List<Proyecto> obtenerTodos() {
        return proyectoRepository.findAllConCreador();
    }

    @Transactional(readOnly = true)
    public Optional<Proyecto> obtenerPorId(Long id) {
        return proyectoRepository.findByIdConCreador(id);
    }

    @Transactional
    public Optional<Proyecto> actualizarProyecto(Long id, ProyectoRequest request) {
        return proyectoRepository.findByIdConCreador(id).map(proyecto -> {
            ProyectoMapper.updateEntity(proyecto, request);
            return proyectoRepository.save(proyecto);
        });
    }

    public void eliminarProyecto(Long id) {
        proyectoRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public Page<Proyecto> obtenerProyectosFiltrados(String estado, String busqueda, Pageable pageable) {
        EstadoProyecto estadoEnum = parseEstadoProyecto(estado);

        if (estadoEnum != null && busqueda != null && !busqueda.trim().isEmpty()) {
            return proyectoRepository.findByEstadoAndBusquedaConCreadorPaginated(estadoEnum, busqueda.trim(), pageable);
        } else if (estadoEnum != null) {
            return proyectoRepository.findByEstadoConCreadorPaginated(estadoEnum, pageable);
        } else if (busqueda != null && !busqueda.trim().isEmpty()) {
            return proyectoRepository.findByBusquedaConCreadorPaginated(busqueda.trim(), pageable);
        } else {
            return proyectoRepository.findAllConCreadorPaginated(pageable);
        }
    }

    @Transactional(readOnly = true)
    public Page<Proyecto> obtenerProyectosPorUsuario(Long usuarioId, String estado, String busqueda, Pageable pageable) {
        if (usuarioId == null) {
            throw new IllegalArgumentException("El ID del usuario no puede ser nulo");
        }

        EstadoProyecto estadoEnum = parseEstadoProyecto(estado);
        boolean tieneEstado = estadoEnum != null;
        boolean tieneBusqueda = busqueda != null && !busqueda.trim().isEmpty();

        // Determinar qué método del repositorio usar según los filtros aplicados
        if (tieneEstado && tieneBusqueda) {
            logger.debug("Buscando proyectos del usuario {} con estado {} y búsqueda '{}'",
                    usuarioId, estadoEnum, busqueda.trim());
            return proyectoRepository.findByUsuarioIdAndEstadoAndBusquedaConCreadorPaginated(
                    usuarioId, estadoEnum, busqueda.trim(), pageable);
        } else if (tieneEstado) {
            logger.debug("Buscando proyectos del usuario {} con estado {}", usuarioId, estadoEnum);
            return proyectoRepository.findByUsuarioIdAndEstadoConCreadorPaginated(usuarioId, estadoEnum, pageable);
        } else if (tieneBusqueda) {
            logger.debug("Buscando proyectos del usuario {} con búsqueda '{}'", usuarioId, busqueda.trim());
            return proyectoRepository.findByUsuarioIdAndBusquedaConCreadorPaginated(usuarioId, busqueda.trim(), pageable);
        } else {
            logger.debug("Obteniendo todos los proyectos del usuario {}", usuarioId);
            return proyectoRepository.findByUsuarioIdConCreadorPaginated(usuarioId, pageable);
        }
    }

    private EstadoProyecto parseEstadoProyecto(String estado) {
        if (estado == null || estado.trim().isEmpty() || "TODOS".equals(estado)) {
            return null;
        }

        try {
            return EstadoProyecto.valueOf(estado.trim().toUpperCase());
        } catch (IllegalArgumentException e) {
            logger.warn("Estado de proyecto inválido recibido: '{}'. Estados válidos: {}. " +
                            "Se ignorará el filtro de estado.",
                    estado,
                    Arrays.toString(EstadoProyecto.values()));
            return null;
        }
    }
}