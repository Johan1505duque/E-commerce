package com.ecommerce.backend.repository;

import com.ecommerce.backend.model.Inventario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public interface InventarioRepository extends JpaRepository<Inventario, Long> {
    // Buscar inventario por producto
    Optional<Inventario> findByProductoId(Long productoId);

    // Verificar si existe inventario para un producto
    boolean existsByProductoId(Long productoId);

    // Buscar productos con stock bajo
    List<Inventario> findByCantidadLessThan(Integer cantidad);

    // Buscar por ubicación
    List<Inventario> findByUbicacionBodega(String ubicacion);
}
