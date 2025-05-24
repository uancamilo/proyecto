package com.proyecto.integrador.controller;

import com.proyecto.integrador.dto.InvitacionRequest;
import com.proyecto.integrador.dto.InvitacionResponse;
import com.proyecto.integrador.mapper.InvitacionMapper;
import com.proyecto.integrador.model.*;
import com.proyecto.integrador.repository.ProyectoRepository;
import com.proyecto.integrador.repository.UsuarioRepository;
import com.proyecto.integrador.service.InvitacionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.*;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/invitaciones")
@Tag(name = "Invitaciones", description = "Gestión de invitaciones a proyectos")
public class InvitacionController {

    private final InvitacionService invitacionService;
    private final ProyectoRepository proyectoRepository;
    private final UsuarioRepository usuarioRepository;

    public InvitacionController(InvitacionService invitacionService, ProyectoRepository proyectoRepository, UsuarioRepository usuarioRepository) {
        this.invitacionService = invitacionService;
        this.proyectoRepository = proyectoRepository;
        this.usuarioRepository = usuarioRepository;
    }

    @Operation(
            summary = "Enviar una invitación",
            description = "Envía una invitación a un usuario para colaborar en un proyecto.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = InvitacionRequest.class)
                    )
            ),
            responses = {
                    @ApiResponse(responseCode = "201", description = "Invitación enviada correctamente", content = @Content(schema = @Schema(implementation = InvitacionResponse.class))),
                    @ApiResponse(responseCode = "404", description = "Proyecto no encontrado")
            }
    )
    @PostMapping
    public ResponseEntity<?> invitar(@RequestBody InvitacionRequest request,
                                     @AuthenticationPrincipal UserDetails userDetails) {

        Optional<Proyecto> proyectoOpt = proyectoRepository.findById(request.getProyectoId());
        Optional<Usuario> invitadorOpt = usuarioRepository.findByEmail(userDetails.getUsername());

        if (proyectoOpt.isEmpty() || invitadorOpt.isEmpty()) {
            return ResponseEntity.status(404).body("Proyecto o usuario no encontrado");
        }

        Invitacion invitacion = new Invitacion(
                proyectoOpt.get(),
                request.getEmail(),
                invitadorOpt.get(),
                EstadoInvitacion.PENDIENTE
        );

        Invitacion guardada = invitacionService.guardar(invitacion);
        return ResponseEntity.status(201).body(InvitacionMapper.toResponse(guardada));
    }

    @Operation(
            summary = "Listar invitaciones recibidas",
            description = "Obtiene todas las invitaciones que ha recibido el usuario autenticado.",
            responses = @ApiResponse(
                    responseCode = "200",
                    description = "Lista de invitaciones",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = InvitacionResponse.class)))
            )
    )
    @GetMapping("/recibidas")
    public ResponseEntity<?> misInvitaciones(@AuthenticationPrincipal UserDetails userDetails) {
        List<InvitacionResponse> invitaciones = invitacionService
                .obtenerPorEmail(userDetails.getUsername())
                .stream()
                .map(InvitacionMapper::toResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(invitaciones);
    }

    @Operation(
            summary = "Aceptar invitación",
            description = "Permite al usuario aceptar una invitación",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Invitación aceptada"),
                    @ApiResponse(responseCode = "404", description = "Invitación no encontrada")
            }
    )
    @PutMapping("/{id}/aceptar")
    public ResponseEntity<?> aceptar(@PathVariable Long id) {
        Optional<Invitacion> invitacion = invitacionService.obtenerPorProyectoYEmail(id, null); // validación real se mejora luego
        if (invitacion.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        invitacionService.cambiarEstado(id, EstadoInvitacion.ACEPTADA);
        return ResponseEntity.ok().body("Invitación aceptada");
    }

    @Operation(
            summary = "Rechazar invitación",
            description = "Permite al usuario rechazar una invitación",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Invitación rechazada"),
                    @ApiResponse(responseCode = "404", description = "Invitación no encontrada")
            }
    )
    @PutMapping("/{id}/rechazar")
    public ResponseEntity<?> rechazar(@PathVariable Long id) {
        Optional<Invitacion> invitacion = invitacionService.obtenerPorProyectoYEmail(id, null);
        if (invitacion.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        invitacionService.cambiarEstado(id, EstadoInvitacion.RECHAZADA);
        return ResponseEntity.ok().body("Invitación rechazada");
    }
}
