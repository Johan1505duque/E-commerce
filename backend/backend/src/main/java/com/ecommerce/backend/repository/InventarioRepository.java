package com.ecommerce.backend.repository;

import com.ecommerce.backend.model.Inventario;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface InventarioRepository extends MongoRepository<Inventario, String> {
    Optional<Inventario> findByProductoId(Long productoId);
    boolean existsByProductoId(Long productoId);
    List<Inventario> findByCantidadLessThan(Integer cantidad);
}
