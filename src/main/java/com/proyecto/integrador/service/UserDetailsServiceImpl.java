package com.proyecto.integrador.service;

import com.proyecto.integrador.model.Usuario;
import com.proyecto.integrador.model.UsuarioAdmin;
import com.proyecto.integrador.model.UsuarioUser;
import com.proyecto.integrador.repository.UsuarioRepository;
import com.proyecto.integrador.security.CustomUserDetails;
import com.proyecto.integrador.security.SecurityConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UsuarioRepository usuarioRepository;

    @Autowired
    public UserDetailsServiceImpl(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Usuario usuario = usuarioRepository.findByEmail(email.trim().toLowerCase())
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));

        String rol = usuario instanceof UsuarioAdmin ? "ADMIN" : "USER";
        GrantedAuthority authority = new SimpleGrantedAuthority("ROLE_" + rol);

        return new CustomUserDetails(usuario, Collections.singletonList(authority));
    }
}
