package com.ecommerce.backend.repository;

import com.ecommerce.backend.model.Envio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EnvioRepository extends JpaRepository<Envio, Long> {
    Optional<Envio> findByOrdenId(Long ordenId);
    Optional<Envio> findByCodigoRastreo(String codigoRastreo);
    boolean existsByOrdenId(Long ordenId);
}
