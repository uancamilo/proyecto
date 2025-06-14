package com.proyecto.integrador.controller;

import com.proyecto.integrador.dto.ProyectoRequest;
import com.proyecto.integrador.dto.ProyectoResponse;
import com.proyecto.integrador.mapper.ProyectoMapper;
import com.proyecto.integrador.model.Proyecto;
import com.proyecto.integrador.model.Usuario;
import com.proyecto.integrador.repository.UsuarioRepository;
import com.proyecto.integrador.service.ProyectoService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
            description = "Permite a cualquier usuario autenticado crear un proyecto con sus detalles y estado inicial.",
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
            summary = "Obtener proyectos con paginación",
            description = "Retorna una lista paginada de proyectos con filtros opcionales.",
            parameters = {
                    @Parameter(name = "page", description = "Número de página (0-based)", example = "0"),
                    @Parameter(name = "size", description = "Tamaño de página", example = "10"),
                    @Parameter(name = "sort", description = "Campo de ordenamiento", example = "fechaPublicacion"),
                    @Parameter(name = "direction", description = "Dirección de ordenamiento", example = "desc"),
                    @Parameter(name = "estado", description = "Filtrar por estado", example = "PUBLICADO"),
                    @Parameter(name = "busqueda", description = "Búsqueda en nombre y descripción", example = "sistema")
            },
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Lista paginada de proyectos",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = Map.class)
                            )
                    )
            }
    )
    @GetMapping("/paginado")
    public ResponseEntity<Map<String, Object>> listarProyectosPaginado(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "fechaPublicacion") String sort,
            @RequestParam(defaultValue = "desc") String direction,
            @RequestParam(required = false) String estado,
            @RequestParam(required = false) String busqueda) {

        if (size > 100) size = 100; // Máximo 100 elementos por página
        if (page < 0) page = 0;

        Sort.Direction sortDirection = direction.equalsIgnoreCase("desc")
                ? Sort.Direction.DESC
                : Sort.Direction.ASC;
        Sort sortBy = Sort.by(sortDirection, sort);

        Pageable pageable = PageRequest.of(page, size, sortBy);

        Page<Proyecto> proyectosPage = proyectoService.obtenerProyectosFiltrados(estado, busqueda, pageable);

        List<ProyectoResponse> proyectos = proyectosPage.getContent().stream()
                .map(ProyectoMapper::toResponse)
                .collect(Collectors.toList());

        Map<String, Object> response = new HashMap<>();
        response.put("proyectos", proyectos);
        response.put("currentPage", proyectosPage.getNumber());
        response.put("totalItems", proyectosPage.getTotalElements());
        response.put("totalPages", proyectosPage.getTotalPages());
        response.put("pageSize", proyectosPage.getSize());
        response.put("hasNext", proyectosPage.hasNext());
        response.put("hasPrevious", proyectosPage.hasPrevious());
        response.put("isFirst", proyectosPage.isFirst());
        response.put("isLast", proyectosPage.isLast());

        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "Obtener mis proyectos con paginación",
            description = "Retorna una lista paginada de los proyectos creados por el usuario autenticado.",
            parameters = {
                    @Parameter(name = "page", description = "Número de página (0-based)", example = "0"),
                    @Parameter(name = "size", description = "Tamaño de página", example = "10"),
                    @Parameter(name = "sort", description = "Campo de ordenamiento", example = "fechaPublicacion"),
                    @Parameter(name = "direction", description = "Dirección de ordenamiento", example = "desc"),
                    @Parameter(name = "estado", description = "Filtrar por estado", example = "PUBLICADO"),
                    @Parameter(name = "busqueda", description = "Búsqueda en nombre y descripción", example = "sistema")
            },
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Lista paginada de mis proyectos",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = Map.class)
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
    @GetMapping("/mis-proyectos/paginado")
    public ResponseEntity<Map<String, Object>> listarMisProyectosPaginado(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "fechaPublicacion") String sort,
            @RequestParam(defaultValue = "desc") String direction,
            @RequestParam(required = false) String estado,
            @RequestParam(required = false) String busqueda,
            @AuthenticationPrincipal UserDetails userDetails) {

        // Validar usuario autenticado
        Optional<Usuario> usuario = usuarioRepository.findByEmail(userDetails.getUsername());
        if (usuario.isEmpty()) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", "Usuario no autenticado");
            return ResponseEntity.status(401).body(errorResponse);
        }

        // Validar parámetros
        if (size > 100) size = 100; // Máximo 100 elementos por página
        if (page < 0) page = 0;

        Sort.Direction sortDirection = direction.equalsIgnoreCase("desc")
                ? Sort.Direction.DESC
                : Sort.Direction.ASC;
        Sort sortBy = Sort.by(sortDirection, sort);

        Pageable pageable = PageRequest.of(page, size, sortBy);

        // Obtener proyectos del usuario específico
        Page<Proyecto> proyectosPage = proyectoService.obtenerProyectosPorUsuario(
                usuario.get().getId(), estado, busqueda, pageable);

        List<ProyectoResponse> proyectos = proyectosPage.getContent().stream()
                .map(ProyectoMapper::toResponse)
                .collect(Collectors.toList());

        Map<String, Object> response = new HashMap<>();
        response.put("proyectos", proyectos);
        response.put("currentPage", proyectosPage.getNumber());
        response.put("totalItems", proyectosPage.getTotalElements());
        response.put("totalPages", proyectosPage.getTotalPages());
        response.put("pageSize", proyectosPage.getSize());
        response.put("hasNext", proyectosPage.hasNext());
        response.put("hasPrevious", proyectosPage.hasPrevious());
        response.put("isFirst", proyectosPage.isFirst());
        response.put("isLast", proyectosPage.isLast());

        return ResponseEntity.ok(response);
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
            description = "Permite modificar los datos de un proyecto existente si el usuario es administrador o el creador del proyecto.",
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
                            responseCode = "403",
                            description = "Sin permisos para editar este proyecto",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(example = "{\"error\": \"No tienes permisos para editar este proyecto\"}")
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
    public ResponseEntity<?> actualizarProyecto(@PathVariable Long id,
                                                @RequestBody ProyectoRequest request,
                                                @AuthenticationPrincipal UserDetails userDetails) {

        // Buscar el usuario autenticado
        Optional<Usuario> usuarioOpt = usuarioRepository.findByEmail(userDetails.getUsername());
        if (usuarioOpt.isEmpty()) {
            return ResponseEntity.status(401).body("Usuario no autenticado");
        }

        Usuario usuario = usuarioOpt.get();

        // Buscar el proyecto
        Optional<Proyecto> proyectoOpt = proyectoService.obtenerPorId(id);
        if (proyectoOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Proyecto proyecto = proyectoOpt.get();

        // Verificar permisos: debe ser admin O el creador del proyecto
        boolean esAdmin = "ADMIN".equals(usuario.getRol());
        boolean esCreador = proyecto.getCreadoPor().getId().equals(usuario.getId());

        if (!esAdmin && !esCreador) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", "No tienes permisos para editar este proyecto");
            return ResponseEntity.status(403).body(errorResponse);
        }

        return proyectoService.actualizarProyecto(id, request)
                .map(proyectoActualizado -> ResponseEntity.ok(ProyectoMapper.toResponse(proyectoActualizado)))
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(
            summary = "Eliminar un proyecto",
            description = "Permite a un administrador o al creador del proyecto eliminarlo.",
            responses = {
                    @ApiResponse(
                            responseCode = "204",
                            description = "Proyecto eliminado correctamente"
                    ),
                    @ApiResponse(
                            responseCode = "403",
                            description = "Sin permisos para eliminar este proyecto",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(example = "{\"error\": \"No tienes permisos para eliminar este proyecto\"}")
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
    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminarProyecto(@PathVariable Long id,
                                              @AuthenticationPrincipal UserDetails userDetails) {

        // Buscar el usuario autenticado
        Optional<Usuario> usuarioOpt = usuarioRepository.findByEmail(userDetails.getUsername());
        if (usuarioOpt.isEmpty()) {
            return ResponseEntity.status(401).body("Usuario no autenticado");
        }

        Usuario usuario = usuarioOpt.get();

        // Buscar el proyecto
        Optional<Proyecto> proyectoOpt = proyectoService.obtenerPorId(id);
        if (proyectoOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Proyecto proyecto = proyectoOpt.get();

        // Verificar permisos: debe ser admin O el creador del proyecto
        boolean esAdmin = "ADMIN".equals(usuario.getRol());
        boolean esCreador = proyecto.getCreadoPor().getId().equals(usuario.getId());

        if (!esAdmin && !esCreador) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", "No tienes permisos para eliminar este proyecto");
            return ResponseEntity.status(403).body(errorResponse);
        }

        proyectoService.eliminarProyecto(id);
        return ResponseEntity.noContent().build();
    }
}