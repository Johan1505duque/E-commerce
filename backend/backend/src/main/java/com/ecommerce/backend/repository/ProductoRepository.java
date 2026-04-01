package com.ecommerce.backend.repository;

import com.ecommerce.backend.model.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface ProductoRepository extends JpaRepository<Producto, Long> {

    // Buscar por nombre exacto
    Optional<Producto> findByNombre(String nombre);

    // Buscar por nombre parcial (como un buscador)
    List<Producto> findByNombreContainingIgnoreCase(String nombre);

    // Listar solo activos
    List<Producto> findByActivo(Boolean activo);

    // Buscar por id y activo
    Optional<Producto> findByIdAndActivo(Long id, Boolean activo);

    // Verificar si existe por nombre
    boolean existsByNombre(String nombre);

    // Buscar activos por rango de precio
    List<Producto> findByActivoAndPrecioBetween(
            Boolean activo, BigDecimal precioMin, BigDecimal precioMax);
}
