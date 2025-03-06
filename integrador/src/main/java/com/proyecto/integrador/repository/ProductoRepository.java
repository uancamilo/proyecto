package com.proyecto.integrador.repository;

import com.proyecto.integrador.model.Producto;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductoRepository extends JpaRepository <Producto, Long>{
}
