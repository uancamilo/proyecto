package com.proyecto.integrador.repository;

import com.proyecto.integrador.model.Invitacion;
import com.proyecto.integrador.model.EstadoInvitacion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface InvitacionRepository extends JpaRepository<Invitacion, Long> {

    List<Invitacion> findByInvitadoEmail(String email);

    List<Invitacion> findByProyectoId(Long proyectoId);

    Optional<Invitacion> findByProyectoIdAndInvitadoEmail(Long proyectoId, String email);

    List<Invitacion> findByInvitadoEmailAndEstado(String email, EstadoInvitacion estado);

    boolean existsByProyectoIdAndInvitadoEmailAndEstado(Long proyectoId, String invitadoEmail, EstadoInvitacion estado);
}
