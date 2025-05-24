package com.proyecto.integrador.model;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("ADMIN")
public class UsuarioAdmin extends Usuario {

    public UsuarioAdmin() {
        super();
    }

    public UsuarioAdmin(String nombre, String email, String password) {
        super(nombre, email, password);
    }

    @Override
    public String getRol() {
        return "ROLE_ADMIN";
    }
}

