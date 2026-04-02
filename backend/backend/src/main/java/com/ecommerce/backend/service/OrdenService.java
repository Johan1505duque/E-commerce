package com.ecommerce.backend.service;

import com.ecommerce.backend.dto.ComOrdenDTO.CrearOrdenDTO;
import com.ecommerce.backend.dto.ComOrdenDTO.OrdenDTO;
import com.ecommerce.backend.model.Enum.EstadoOrden;

import java.util.List;

public interface OrdenService {
    OrdenDTO crearOrden(CrearOrdenDTO dto);
    OrdenDTO buscarPorId(Long id);
    List<OrdenDTO> listarPorUsuario(Long usuarioId);
    List<OrdenDTO> listarPorEstado(EstadoOrden estado);
    OrdenDTO cambiarEstado(Long id, EstadoOrden estado);
    OrdenDTO cancelarOrden(Long id);
}
