package com.proyecto.integrador.mapper;

import com.proyecto.integrador.dto.InvitacionResponse;
import com.proyecto.integrador.model.Invitacion;

public class InvitacionMapper {

    public static InvitacionResponse toResponse(Invitacion invitacion) {
        return new InvitacionResponse(
                invitacion.getId(),
                invitacion.getProyecto().getNombre(),
                invitacion.getInvitadoEmail(),
                invitacion.getInvitador().getEmail(),
                invitacion.getEstado().name(),
                invitacion.getFechaInvitacion()
        );
    }
}
