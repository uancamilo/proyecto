package com.proyecto.integrador.service;

import com.proyecto.integrador.model.*;
import com.proyecto.integrador.repository.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class DataSeederService {

    private static final Logger log = LoggerFactory.getLogger(DataSeederService.class);

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private ProyectoRepository proyectoRepository;

    @Autowired
    private InvitacionRepository invitacionRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Value("${app.data.seed.enabled:false}")
    private boolean seedEnabled;

    @Value("${app.data.seed.users.count:20}")
    private int usersCount;

    @Value("${app.data.seed.projects.count:10}")
    private int projectsCount;

    @Value("${app.data.seed.invitations.count:30}")
    private int invitationsCount;

    private final Random random = new Random(42);

    @EventListener(ApplicationReadyEvent.class)
    public void onApplicationReady() {
        log.info("🔧 DataSeeder verificando configuración...");
        log.info("🔧 seedEnabled: {}", seedEnabled);
        log.info("🔧 Usuarios en BD: {}", usuarioRepository.count());
        log.info("🔧 Proyectos en BD: {}", proyectoRepository.count());

        if (seedEnabled) {
            log.info("🌱 DataSeeder HABILITADO - Iniciando...");
            try {
                seedDummyData();
            } catch (Exception e) {
                log.error("❌ Error en DataSeeder: {}", e.getMessage(), e);
            }
        } else {
            log.info("⏸️ DataSeeder DESHABILITADO (app.data.seed.enabled=false)");
        }
    }

    // Problema: Esta condición en DataSeederService.java impide la ejecución:
// if (totalProyectos < 5) {

// SOLUCION 1: Modificar la condición en DataSeederService.java

    @Transactional
    public void seedDummyData() {
        log.info("🔍 Verificando si necesita seed...");

        long totalUsuarios = usuarioRepository.count();
        long totalProyectos = proyectoRepository.count();

        log.info("📊 Estado actual: {} usuarios, {} proyectos", totalUsuarios, totalProyectos);

        // CAMBIAR ESTA LÍNEA:
        // if (totalProyectos < 5) {

        // POR ESTA (para permitir más datos):
        if (totalProyectos < projectsCount) {
            log.info("🚀 Iniciando seed de datos adicionales...");
            log.info("🎯 Objetivo: {} usuarios, {} proyectos, {} invitaciones",
                    usersCount, projectsCount, invitationsCount);

            try {
                // 1. Crear usuarios adicionales
                List<Usuario> nuevosUsuarios = createAdditionalUsers();
                log.info("✅ Usuarios creados: {}", nuevosUsuarios.size());

                // 2. Crear proyectos adicionales
                List<Proyecto> nuevosProyectos = createAdditionalProjects();
                log.info("✅ Proyectos creados: {}", nuevosProyectos.size());

                // 3. Crear invitaciones adicionales
                List<Invitacion> nuevasInvitaciones = createAdditionalInvitations();
                log.info("✅ Invitaciones creadas: {}", nuevasInvitaciones.size());

            } catch (Exception e) {
                log.error("❌ Error durante seed adicional: {}", e.getMessage(), e);
                throw e;
            }
        } else {
            log.info("⏭️ Seed saltado - Ya hay {} proyectos (objetivo: {})", totalProyectos, projectsCount);
        }
    }

// METODOS MODIFICADOS para crear datos adicionales:

    private List<Usuario> createAdditionalUsers() {
        List<Usuario> usuarios = new ArrayList<>();

        // Array expandido de nombres
        String[] nombres = {
                "Ana García", "Carlos López", "María Rodríguez", "Juan Martínez",
                "Laura Sánchez", "Pedro Gómez", "Carmen Hernández", "Luis Pérez",
                "Sofía Ruiz", "Miguel Torres", "Elena Moreno", "Diego Silva",
                "Isabel Castro", "Fernando Jiménez", "Claudia Vargas", "Roberto Herrera",
                "Patricia Ramos", "Andrés Delgado", "Natalia Mendoza", "Ricardo Aguilar",
                "Verónica Cruz", "Javier Rojas", "Alejandra Peña", "Sebastián Vega",
                "Mónica Guerrero", "Daniel Flores", "Gabriela Ortiz", "Adrián Mora",
                "Lorena Castillo", "Óscar Romero", "Valeria Espinoza", "Emilio Reyes",
                "Cristina Ríos", "Mauricio Lozano", "Sandra Medina", "Raúl Paredes",
                "Beatriz Salinas", "Francisco Núñez", "Mariana Contreras", "Jorge Campos",
                "Paola Miranda", "Alberto Serrano", "Lucía Vázquez", "Guillermo León",
                "Silvia Carrillo", "Ignacio Montes", "Carolina Ibarra", "Rodrigo Fuentes",
                "Andrea Villalobos", "Pablo Ramírez", "Fernanda Gutiérrez", "Arturo Molina",
                "Rocío Navarro", "Héctor Cortés", "Daniela Rueda", "César Cabrera",
                "Gloria Sandoval", "Mario Figueroa", "Esperanza Marín", "Víctor Domínguez",
                "Pilar Alvarez", "Enrique Soto", "Mercedes Villa", "Antonio Paz",
                "Rosa Acosta", "Manuel Benítez", "Alma Estrada", "Salvador Orozco",
                "Norma Pacheco", "Rubén Maldonado", "Yolanda Cervantes", "Joaquín Santana",
                "Leticia Ugalde", "Ramón Solís", "Irma Gallardo", "Tomás Mejía",
                "Amparo Cuevas", "Edgar Rosales", "Dolores Velasco", "Nicolás Ayala",
                "Consuelo Ponce", "Agustín Cárdenas", "Remedios Chávez", "Felipe Escobar"
        };

        String[] dominios = {"gmail.com", "hotmail.com", "yahoo.com", "outlook.com", "empresa.com", "test.com"};

        // Calcular cuántos usuarios crear
        long usuariosExistentes = usuarioRepository.count();
        int usuariosACrear = Math.max(0, usersCount - (int)usuariosExistentes);

        log.info("📝 Creando {} usuarios adicionales", usuariosACrear);

        for (int i = 0; i < usuariosACrear; i++) {
            try {
                String nombreCompleto = nombres[i % nombres.length];
                if (i >= nombres.length) {
                    nombreCompleto += " " + (i / nombres.length + 1);
                }

                // Email único basado en timestamp para evitar duplicados
                String email = "usuario" + System.currentTimeMillis() + "_" + i + "@" + dominios[i % dominios.length];

                // Verificar que no exista
                if (!usuarioRepository.existsByEmail(email)) {
                    UsuarioUser usuario = new UsuarioUser();
                    usuario.setNombre(nombreCompleto);
                    usuario.setEmail(email);
                    usuario.setPassword(passwordEncoder.encode("password123"));

                    Usuario savedUser = usuarioRepository.save(usuario);
                    usuarios.add(savedUser);

                    if (i % 50 == 0) {
                        log.debug("👤 Usuarios creados: {}/{}", i + 1, usuariosACrear);
                    }
                }
            } catch (Exception e) {
                log.warn("Error creando usuario {}: {}", i, e.getMessage());
            }
        }

        return usuarios;
    }

    private List<Proyecto> createAdditionalProjects() {
        List<Proyecto> proyectos = new ArrayList<>();

        // Obtener admin para asignar como creador
        Usuario admin = usuarioRepository.findByEmail("admin@email.com")
                .orElse(null);

        if (admin == null) {
            log.warn("⚠️ No se encontró admin, no se pueden crear proyectos");
            return proyectos;
        }

        // Array expandido de proyectos
        String[] nombresProyectos = {
                "Sistema de Gestión Escolar", "E-commerce de Ropa", "App de Delivery",
                "Portal de Noticias", "Sistema de Reservas", "Dashboard Analytics",
                "Red Social Corporativa", "Sistema de Inventario", "Plataforma de Cursos",
                "App de Fitness", "Sistema de Facturación", "Marketplace de Servicios",
                "Sistema de Help Desk", "App de Transporte", "Portal Inmobiliario",
                "Sistema de Biblioteca", "App de Citas", "Portal de Empleo",
                "Sistema de Telemedicina", "App de Finanzas Personales", "CRM Empresarial",
                "Sistema de Punto de Venta", "Plataforma de Streaming", "App de Música",
                "Sistema de Gestión Hotelera", "Portal de Turismo", "App de Recetas",
                "Sistema de Control de Asistencia", "Plataforma de Freelancers", "App de Meditación",
                "Sistema de Gestión Médica", "Portal de Eventos", "App de Fotografía",
                "Sistema de Logística", "Plataforma Educativa", "App de Productividad",
                "Sistema de Recursos Humanos", "Portal de Arte", "App de Deportes",
                "Sistema de Contabilidad", "Plataforma de Marketing", "App de Viajes",
                "Sistema de Gestión Agrícola", "Portal de Tecnología", "App de Clima",
                "Sistema de Seguridad", "Plataforma de Blockchain", "App de IoT",
                "Sistema de Manufactura", "Portal de Sostenibilidad", "App de Realidad Virtual",
                "Sistema de Telecomunicaciones", "Plataforma de IA", "App de Machine Learning",
                "Sistema de Big Data", "Portal de Ciberseguridad", "App de Cloud Computing",
                "Sistema de DevOps", "Plataforma de Microservicios", "App de APIs",
                "Sistema de Business Intelligence", "Portal de Data Science", "App de Analytics"
        };

        String[] sectores = {
                "Educativo", "Comercial", "Salud", "Financiero", "Logística", "Entretenimiento",
                "Turismo", "Alimentación", "Tecnología", "Manufactura", "Agricultura", "Inmobiliario"
        };

        EstadoProyecto[] estados = EstadoProyecto.values();

        // Calcular cuántos proyectos crear
        long proyectosExistentes = proyectoRepository.count();
        int proyectosACrear = Math.max(0, projectsCount - (int)proyectosExistentes);

        log.info("📝 Creando {} proyectos adicionales", proyectosACrear);

        for (int i = 0; i < proyectosACrear; i++) {
            try {
                Proyecto proyecto = new Proyecto();

                String nombreBase = nombresProyectos[i % nombresProyectos.length];
                String sector = sectores[i % sectores.length];
                proyecto.setNombre(nombreBase + " " + sector + (i >= nombresProyectos.length ? " v" + (i / nombresProyectos.length + 1) : ""));

                proyecto.setDescripcion("Desarrollo completo de " + nombreBase.toLowerCase() +
                        " para el sector " + sector.toLowerCase() +
                        ". Incluye diseño, desarrollo, testing y deployment.");

                proyecto.setEstado(estados[random.nextInt(estados.length)]);

                // Fechas históricas más realistas (últimos 6 meses)
                LocalDate fechaPublicacion = LocalDate.now().minusDays(random.nextInt(180));
                proyecto.setFechaPublicacion(fechaPublicacion);
                proyecto.setFechaPostulacion(fechaPublicacion.plusDays(7 + random.nextInt(21)));
                proyecto.setFechaEntrega(proyecto.getFechaPostulacion().plusDays(30 + random.nextInt(120)));

                // Valores más realistas con distribución variada
                BigDecimal[] rangosValor = {
                        BigDecimal.valueOf(1000 + random.nextInt(4000)),    // 1K-5K (proyectos pequeños)
                        BigDecimal.valueOf(5000 + random.nextInt(10000)),   // 5K-15K (proyectos medianos)
                        BigDecimal.valueOf(15000 + random.nextInt(35000)),  // 15K-50K (proyectos grandes)
                        BigDecimal.valueOf(50000 + random.nextInt(50000))   // 50K-100K (proyectos enterprise)
                };
                proyecto.setValorMonetario(rangosValor[random.nextInt(rangosValor.length)]);

                proyecto.setCreadoPor(admin);

                Proyecto savedProject = proyectoRepository.save(proyecto);
                proyectos.add(savedProject);

                if (i % 20 == 0) {
                    log.debug("🏗️ Proyectos creados: {}/{}", i + 1, proyectosACrear);
                }

            } catch (Exception e) {
                log.warn("Error creando proyecto {}: {}", i, e.getMessage());
            }
        }

        return proyectos;
    }

    private List<Invitacion> createAdditionalInvitations() {
        List<Invitacion> invitaciones = new ArrayList<>();

        try {
            // Obtener datos necesarios
            List<Usuario> usuarios = usuarioRepository.findAll();
            List<Proyecto> proyectos = proyectoRepository.findAll();
            Usuario admin = usuarioRepository.findByEmail("admin@email.com").orElse(null);

            if (usuarios.isEmpty() || proyectos.isEmpty() || admin == null) {
                log.warn("⚠️ No hay datos suficientes para crear invitaciones");
                return invitaciones;
            }

            // Filtrar solo usuarios normales para invitar
            List<Usuario> usuariosNormales = usuarios.stream()
                    .filter(u -> u instanceof UsuarioUser)
                    .toList();

            if (usuariosNormales.isEmpty()) {
                log.warn("⚠️ No hay usuarios normales para invitar");
                return invitaciones;
            }

            EstadoInvitacion[] estados = EstadoInvitacion.values();
            Set<String> invitacionesUnicas = new HashSet<>();

            // Obtener invitaciones existentes para evitar duplicados
            List<Invitacion> existentes = invitacionRepository.findAll();
            for (Invitacion inv : existentes) {
                invitacionesUnicas.add(inv.getProyecto().getId() + "-" + inv.getInvitadoEmail());
            }

            long invitacionesExistentes = invitacionRepository.count();
            int invitacionesACrear = Math.max(0, invitationsCount - (int)invitacionesExistentes);

            log.info("📝 Creando {} invitaciones adicionales", invitacionesACrear);

            int intentos = 0;
            int maxIntentos = invitacionesACrear * 3; // Para evitar loop infinito

            while (invitaciones.size() < invitacionesACrear && intentos < maxIntentos) {
                try {
                    Proyecto proyecto = proyectos.get(random.nextInt(proyectos.size()));
                    Usuario usuario = usuariosNormales.get(random.nextInt(usuariosNormales.size()));

                    String key = proyecto.getId() + "-" + usuario.getEmail();

                    if (!invitacionesUnicas.contains(key)) {
                        invitacionesUnicas.add(key);

                        Invitacion invitacion = new Invitacion();
                        invitacion.setProyecto(proyecto);
                        invitacion.setInvitadoEmail(usuario.getEmail());
                        invitacion.setInvitador(admin);

                        // Distribución realista: 60% aceptadas, 25% pendientes, 15% rechazadas
                        EstadoInvitacion estado;
                        int rand = random.nextInt(100);
                        if (rand < 60) {
                            estado = EstadoInvitacion.ACEPTADA;
                        } else if (rand < 85) {
                            estado = EstadoInvitacion.PENDIENTE;
                        } else {
                            estado = EstadoInvitacion.RECHAZADA;
                        }
                        invitacion.setEstado(estado);

                        // Fechas históricas (últimos 4 meses)
                        LocalDateTime fechaInvitacion = LocalDateTime.now()
                                .minusDays(random.nextInt(120))
                                .minusHours(random.nextInt(24))
                                .minusMinutes(random.nextInt(60));
                        invitacion.setFechaInvitacion(fechaInvitacion);

                        Invitacion savedInvitation = invitacionRepository.save(invitacion);
                        invitaciones.add(savedInvitation);

                        if (invitaciones.size() % 100 == 0) {
                            log.debug("📧 Invitaciones creadas: {}/{}", invitaciones.size(), invitacionesACrear);
                        }
                    }
                } catch (Exception e) {
                    log.warn("Error creando invitación {}: {}", intentos, e.getMessage());
                }
                intentos++;
            }
        } catch (Exception e) {
            log.error("Error general creando invitaciones: {}", e.getMessage());
        }

        return invitaciones;
    }
}