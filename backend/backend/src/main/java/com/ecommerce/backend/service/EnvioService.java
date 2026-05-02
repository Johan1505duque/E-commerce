package com.ecommerce.backend.service;

import com.ecommerce.backend.dto.CrearEnvioDTO;
import com.ecommerce.backend.dto.EnvioDTO;
import com.ecommerce.backend.model.Enum.EstadoEnvio;

import java.util.List;

public interface EnvioService {
    EnvioDTO crearEnvio(CrearEnvioDTO dto);
    EnvioDTO obtenerEnvioPorId(Long id);
    EnvioDTO obtenerEnvioPorOrdenId(Long ordenId);
    EnvioDTO actualizarEstadoEnvio(Long id, EstadoEnvio nuevoEstado);
    EnvioDTO asignarCodigoRastreo(Long id, String codigoRastreo);
    List<EnvioDTO> listarTodosLosEnvios();
}
