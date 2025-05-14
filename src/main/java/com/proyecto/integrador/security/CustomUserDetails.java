package com.proyecto.integrador.security;

import com.proyecto.integrador.model.Usuario;
import com.proyecto.integrador.model.UsuarioAdmin;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

public class CustomUserDetails implements UserDetails {

    private final Usuario usuario;
    private final Collection<? extends GrantedAuthority> authorities;

    public CustomUserDetails(Usuario usuario, Collection<? extends GrantedAuthority> authorities) {
        this.usuario = usuario;
        this.authorities = authorities;
    }

    public String getNombre() {
        return usuario.getNombre();
    }

    public String getEmail() {
        return usuario.getEmail();
    }

    public String getRol() {
        return usuario instanceof UsuarioAdmin ? "ADMIN" : "USER";
    }

    @Override public String getPassword() { return usuario.getPassword(); }
    @Override public String getUsername() { return usuario.getEmail(); }
    @Override public boolean isAccountNonExpired() { return true; }
    @Override public boolean isAccountNonLocked() { return true; }
    @Override public boolean isCredentialsNonExpired() { return true; }
    @Override public boolean isEnabled() { return true; }
    @Override public Collection<? extends GrantedAuthority> getAuthorities() { return authorities; }
}

