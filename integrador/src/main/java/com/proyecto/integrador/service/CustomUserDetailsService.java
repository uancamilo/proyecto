//package com.proyecto.integrador.service;
//
//import com.proyecto.integrador.model.Usuario;
//import com.proyecto.integrador.repository.UsuarioRepository;
//import org.springframework.security.core.userdetails.User;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
//import org.springframework.stereotype.Service;
//
//import java.util.ArrayList;
//
//@Service
//public class CustomUserDetailsService implements UserDetailsService {
//
//    private final UsuarioRepository usuarioRepository;
//
//    public CustomUserDetailsService(UsuarioRepository usuarioRepository) {
//        this.usuarioRepository = usuarioRepository;
//    }
//
//    @Override
//    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//        Usuario usuario = usuarioRepository.findByEmail(username)
//                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));
//        return new User(usuario.getEmail(), usuario.getPassword(), new ArrayList<>());
//    }
//
//}
//
