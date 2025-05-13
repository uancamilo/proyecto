package com.proyecto.integrador.service;

import com.proyecto.integrador.model.UsuarioAdmin;
import com.proyecto.integrador.repository.UsuarioRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UsuarioInitService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UsuarioInitService(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @PostConstruct
    public void init() {
        if (usuarioRepository.count() == 0) {
            UsuarioAdmin admin = new UsuarioAdmin();
            admin.setNombre("Admin");
            admin.setEmail("admin@email.com");
            admin.setPassword(passwordEncoder.encode("admin123"));

            usuarioRepository.save(admin);
        }
    }
}
