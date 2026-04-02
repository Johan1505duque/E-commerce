package com.ecommerce.backend.repository;

import com.ecommerce.backend.model.Enum.EstadoOrden;
import com.ecommerce.backend.model.Orden;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrdenRepository extends JpaRepository<Orden,Long> {
    List<Orden> findByUsuarioId(Long usuarioId);
    List<Orden> findByEstado(EstadoOrden estado);
    List<Orden> findByUsuarioIdAndEstado(Long usuarioId, EstadoOrden estado);
}
