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

    /**
     * Guarda una invitación si no existe ya una pendiente para ese email y proyecto.
     * @throws IllegalArgumentException si ya existe una invitación pendiente para el mismo email y proyecto.
     */
    public Invitacion guardar(Invitacion invitacion) {
        Long proyectoId = invitacion.getProyecto().getId();
        String email = invitacion.getInvitadoEmail();

        boolean yaExiste = invitacionRepository.existsByProyectoIdAndInvitadoEmailAndEstado(
                proyectoId, email, EstadoInvitacion.PENDIENTE
        );

        if (yaExiste) {
            throw new IllegalArgumentException("Ya existe una invitación pendiente para este proyecto y correo.");
        }

        // Establece fecha y estado por defecto si no está definido
        invitacion.setFechaInvitacion(LocalDateTime.now());

        if (invitacion.getEstado() == null) {
            invitacion.setEstado(EstadoInvitacion.PENDIENTE);
        }

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
        invitacionRepository.findById(id).ifPresent(i -> {
            i.setEstado(nuevoEstado);
            invitacionRepository.save(i);
        });
    }
}
