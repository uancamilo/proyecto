package com.proyecto.integrador.repository;

import com.proyecto.integrador.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    Optional<Usuario> findByEmail(String email);
    boolean existsByEmail(String email);
    @Modifying
    @Query("DELETE FROM Usuario u WHERE u.email != 'admin@email.com'")
    void deleteAllExceptAdmin();
}
