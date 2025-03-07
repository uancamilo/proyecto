package com.proyecto.integrador.service;

import com.proyecto.integrador.model.Usuario;
import com.proyecto.integrador.repository.UsuarioRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import jakarta.annotation.PostConstruct;

@Service
public class UsuarioInitService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    public UsuarioInitService(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @PostConstruct
    public void init() {
        if (usuarioRepository.count() == 0) {
            Usuario usuario = new Usuario();
            usuario.setNombre("Admin");
            usuario.setEmail("admin@email.com");
            usuario.setPassword(passwordEncoder.encode("admin123"));
            usuarioRepository.save(usuario);
            System.out.println("Usuario inicial creado: admin@email.com");
        }
    }
}
