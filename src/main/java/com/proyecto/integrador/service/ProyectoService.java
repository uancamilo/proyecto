package com.proyecto.integrador.service;

import com.proyecto.integrador.dto.ProyectoRequest;
import com.proyecto.integrador.mapper.ProyectoMapper;
import com.proyecto.integrador.model.Proyecto;
import com.proyecto.integrador.repository.ProyectoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class ProyectoService {

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
}
