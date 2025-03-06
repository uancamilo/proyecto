package com.proyecto.integrador.service;

import com.proyecto.integrador.model.Usuario;
import com.proyecto.integrador.repository.UsuarioRepository;
import org.springframework.stereotype.Service;
import jakarta.annotation.PostConstruct;

@Service
public class UsuarioInitService {

    private final UsuarioRepository usuarioRepository;

    public UsuarioInitService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
            }

    @PostConstruct
    public void init() {
        if (usuarioRepository.count() == 0) {
            Usuario usuario = new Usuario();
            usuario.setNombre("Admin");
            usuario.setEmail("admin@email.com");
            usuario.setPassword("admin123");
            usuarioRepository.save(usuario);
            System.out.println("Usuario inicial creado: admin@email.com");
        }
    }
}
