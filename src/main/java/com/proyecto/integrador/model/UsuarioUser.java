package com.proyecto.integrador.model;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("USER")
public class UsuarioUser extends Usuario {

    public UsuarioUser() {
        super();
    }

    public UsuarioUser(String nombre, String email, String password) {
        super(nombre, email, password);
    }

    @Override
    public String getRol() {
        return "ROLE_USER";
    }
}
