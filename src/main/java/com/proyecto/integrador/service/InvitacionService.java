package com.proyecto.integrador.service;

import com.proyecto.integrador.model.EstadoInvitacion;
import com.proyecto.integrador.model.Invitacion;
import com.proyecto.integrador.repository.InvitacionRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class InvitacionService {

    private final InvitacionRepository invitacionRepository;

    public InvitacionService(InvitacionRepository invitacionRepository) {
        this.invitacionRepository = invitacionRepository;
    }

    public Invitacion guardar(Invitacion invitacion) {
        invitacion.setFechaInvitacion(LocalDateTime.now());
        return invitacionRepository.save(invitacion);
    }

    public List<Invitacion> obtenerPorEmail(String email) {
        return invitacionRepository.findByInvitadoEmail(email);
    }

    public List<Invitacion> obtenerPorProyecto(Long proyectoId) {
        return invitacionRepository.findByProyectoId(proyectoId);
    }

    public Optional<Invitacion> obtenerPorProyectoYEmail(Long proyectoId, String email) {
        return invitacionRepository.findByProyectoIdAndInvitadoEmail(proyectoId, email);
    }

    public void cambiarEstado(Long id, EstadoInvitacion nuevoEstado) {
        Optional<Invitacion> invitacion = invitacionRepository.findById(id);
        invitacion.ifPresent(i -> {
            i.setEstado(nuevoEstado);
            invitacionRepository.save(i);
        });
    }
}
