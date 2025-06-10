package com.proyecto.integrador.repository;

import com.proyecto.integrador.model.EstadoProyecto;
import com.proyecto.integrador.model.Proyecto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ProyectoRepository extends JpaRepository<Proyecto, Long> {

    @Query("SELECT p FROM Proyecto p JOIN FETCH p.creadoPor")
    List<Proyecto> findAllConCreador();

    @Query("SELECT p FROM Proyecto p JOIN FETCH p.creadoPor WHERE p.id = :id")
    Optional<Proyecto> findByIdConCreador(Long id);

    @Query("SELECT COUNT(p) FROM Proyecto p WHERE p.estado = :estado")
    long countByEstado(@Param("estado") EstadoProyecto estado);

    @Query("SELECT p FROM Proyecto p JOIN FETCH p.creadoPor")
    Page<Proyecto> findAllConCreadorPaginated(Pageable pageable);

    @Query("SELECT p FROM Proyecto p JOIN FETCH p.creadoPor WHERE p.estado = :estado")
    Page<Proyecto> findByEstadoConCreadorPaginated(@Param("estado") EstadoProyecto estado, Pageable pageable);

    @Query("SELECT p FROM Proyecto p JOIN FETCH p.creadoPor WHERE " +
            "LOWER(p.nombre) LIKE LOWER(CONCAT('%', :busqueda, '%')) OR " +
            "LOWER(p.descripcion) LIKE LOWER(CONCAT('%', :busqueda, '%'))")
    Page<Proyecto> findByBusquedaConCreadorPaginated(@Param("busqueda") String busqueda, Pageable pageable);

    @Query("SELECT p FROM Proyecto p JOIN FETCH p.creadoPor WHERE " +
            "p.estado = :estado AND " +
            "(LOWER(p.nombre) LIKE LOWER(CONCAT('%', :busqueda, '%')) OR " +
            "LOWER(p.descripcion) LIKE LOWER(CONCAT('%', :busqueda, '%')))")
    Page<Proyecto> findByEstadoAndBusquedaConCreadorPaginated(
            @Param("estado") EstadoProyecto estado,
            @Param("busqueda") String busqueda,
            Pageable pageable);
}