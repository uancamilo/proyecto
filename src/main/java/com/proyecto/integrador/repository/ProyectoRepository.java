package com.proyecto.integrador.repository;

import com.proyecto.integrador.model.Proyecto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ProyectoRepository extends JpaRepository<Proyecto, Long> {

    @Query("SELECT p FROM Proyecto p JOIN FETCH p.creadoPor")
    List<Proyecto> findAllConCreador();

    @Query("SELECT p FROM Proyecto p JOIN FETCH p.creadoPor WHERE p.id = :id")
    Optional<Proyecto> findByIdConCreador(Long id);
}
