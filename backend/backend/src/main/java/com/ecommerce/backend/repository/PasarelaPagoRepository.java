package com.ecommerce.backend.repository;

import com.ecommerce.backend.model.PasarelaPago;
import com.ecommerce.backend.model.Enum.EstadoPago;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.List;

@Repository
public interface PasarelaPagoRepository extends JpaRepository <PasarelaPago,Long>{
    Optional<PasarelaPago> findByOrdenId(Long ordenId);
    List<PasarelaPago> findByEstado(EstadoPago estado);
    boolean existsByOrdenId(Long ordenId);
}
