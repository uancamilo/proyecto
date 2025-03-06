package com.proyecto.integrador.service;

import com.proyecto.integrador.model.Producto;
import com.proyecto.integrador.repository.ProductoRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;

@Service
public class ProductoInitService {
    private final ProductoRepository productoRepository;

    public ProductoInitService(ProductoRepository productoRepository) {
        this.productoRepository = productoRepository;
    }

    @PostConstruct
    public void init() {
        if (productoRepository.count() == 0) {
            Producto producto = new Producto();
            producto.setNombre("Manzana");
            productoRepository.save(producto);
            System.out.println("Producto guardado con exito");
        }
    }


}
