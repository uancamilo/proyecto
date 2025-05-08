package com.proyecto.integrador.service;

import com.proyecto.integrador.model.Usuario;
import com.proyecto.integrador.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import jakarta.annotation.PostConstruct;

@Service
public class UsuarioInitService {

    private final PasswordEncoder passwordEncoder;
    private final UsuarioRepository usuarioRepository;

    @Autowired
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
            usuario.setRol("ADMIN");
            usuarioRepository.save(usuario);
        }
    }
}
