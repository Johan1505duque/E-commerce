package com.ecommerce.backend.repository;

import com.ecommerce.backend.model.Carrito;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CarritoRepository extends MongoRepository<Carrito, String> {
    Optional<Carrito> findByUsuarioId(Long usuarioId);
    boolean existsByUsuarioId(Long usuarioId);
}
