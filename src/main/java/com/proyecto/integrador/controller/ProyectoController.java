package com.proyecto.integrador.controller;

import com.proyecto.integrador.dto.ProyectoRequest;
import com.proyecto.integrador.dto.ProyectoResponse;
import com.proyecto.integrador.mapper.ProyectoMapper;
import com.proyecto.integrador.model.Proyecto;
import com.proyecto.integrador.model.Usuario;
import com.proyecto.integrador.repository.UsuarioRepository;
import com.proyecto.integrador.service.ProyectoService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/proyectos")
public class ProyectoController {

    private final ProyectoService proyectoService;
    private final UsuarioRepository usuarioRepository;

    public ProyectoController(ProyectoService proyectoService, UsuarioRepository usuarioRepository) {
        this.proyectoService = proyectoService;
        this.usuarioRepository = usuarioRepository;
    }
    @Operation(
            summary = "Crear un nuevo proyecto",
            description = "Permite a un administrador crear un proyecto con sus detalles y estado inicial.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ProyectoRequest.class)
                    )
            ),
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "Proyecto creado exitosamente",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ProyectoResponse.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "Usuario no autenticado",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(example = "{\"error\": \"Usuario no autenticado\"}")
                            )
                    )
            }
    )
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> crearProyecto(@RequestBody ProyectoRequest request,
                                           @AuthenticationPrincipal UserDetails userDetails) {

        Optional<Usuario> usuario = usuarioRepository.findByEmail(userDetails.getUsername());
        if (usuario.isEmpty()) {
            return ResponseEntity.status(401).body("Usuario no autenticado");
        }

        Proyecto proyecto = ProyectoMapper.toEntity(request, usuario.get());
        proyecto.setFechaPublicacion(LocalDate.now());
        Proyecto guardado = proyectoService.crearProyecto(proyecto);
        ProyectoResponse response = ProyectoMapper.toResponse(guardado);

        return ResponseEntity.status(201).body(response);
    }

    @Operation(
            summary = "Obtener la lista de todos los proyectos",
            description = "Retorna una lista de proyectos disponibles, incluyendo su estado, fechas y creador.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Lista de proyectos",
                            content = @Content(
                                    mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = ProyectoResponse.class))
                            )
                    )
            }
    )
    @GetMapping
    public List<ProyectoResponse> listarProyectos() {
        return proyectoService.obtenerTodos().stream()
                .map(ProyectoMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Operation(
            summary = "Obtener un proyecto específico",
            description = "Retorna los detalles de un proyecto dado su ID.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Proyecto encontrado",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ProyectoResponse.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Proyecto no encontrado",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(example = "{\"error\": \"Proyecto no encontrado\"}")
                            )
                    )
            }
    )
    @GetMapping("/{id}")
    public ResponseEntity<?> obtenerProyecto(@PathVariable Long id) {
        return proyectoService.obtenerPorId(id)
                .map(p -> ResponseEntity.ok(ProyectoMapper.toResponse(p)))
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(
            summary = "Actualizar un proyecto existente",
            description = "Permite modificar los datos de un proyecto existente si el usuario es administrador.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ProyectoRequest.class)
                    )
            ),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Proyecto actualizado",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ProyectoResponse.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Proyecto no encontrado",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(example = "{\"error\": \"Proyecto no encontrado\"}")
                            )
                    )
            }
    )
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> actualizarProyecto(@PathVariable Long id,
                                                @RequestBody ProyectoRequest request) {
        return proyectoService.actualizarProyecto(id, request)
                .map(proyecto -> ResponseEntity.ok(ProyectoMapper.toResponse(proyecto)))
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(
            summary = "Eliminar un proyecto",
            description = "Permite a un administrador eliminar un proyecto existente.",
            responses = {
                    @ApiResponse(
                            responseCode = "204",
                            description = "Proyecto eliminado correctamente"
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Proyecto no encontrado",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(example = "{\"error\": \"Proyecto no encontrado\"}")
                            )
                    )
            }
    )
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> eliminarProyecto(@PathVariable Long id) {
        if (proyectoService.obtenerPorId(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        proyectoService.eliminarProyecto(id);
        return ResponseEntity.noContent().build();
    }
}
