package com.ecommerce.backend.service.impl;

import com.ecommerce.backend.dto.CrearEnvioDTO;
import com.ecommerce.backend.dto.EnvioDTO;
import com.ecommerce.backend.exception.BadRequestException;
import com.ecommerce.backend.exception.ResourceNotFoundException;
import com.ecommerce.backend.mapper.EnvioMapper;
import com.ecommerce.backend.model.Envio;
import com.ecommerce.backend.model.Enum.EstadoEnvio;
import com.ecommerce.backend.model.Orden;
import com.ecommerce.backend.repository.EnvioRepository;
import com.ecommerce.backend.repository.OrdenRepository;
import com.ecommerce.backend.service.EnvioService;
import com.ecommerce.backend.service.NotificacionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class EnvioServiceImpl implements EnvioService {

    private final EnvioRepository envioRepository;
    private final OrdenRepository ordenRepository;
    private final EnvioMapper envioMapper;
    private final NotificacionService notificacionService;

    @Override
    public EnvioDTO crearEnvio(CrearEnvioDTO dto) {
        Orden orden = ordenRepository.findById(dto.getOrdenId())
                .orElseThrow(() -> new ResourceNotFoundException("Orden no encontrada con ID: " + dto.getOrdenId()));

        if (envioRepository.existsByOrdenId(dto.getOrdenId())) {
            throw new BadRequestException("Ya existe un envío para la Orden con ID: " + dto.getOrdenId());
        }

        Envio envio = envioMapper.toEntity(dto);
        envio.setOrden(orden);
        envio.setEstado(EstadoEnvio.PENDIENTE);
        envio.setCodigoRastreo(UUID.randomUUID().toString()); // Generar un código de rastreo único
        envio.setCreacion(LocalDateTime.now());
        envio.setActualizacion(LocalDateTime.now());

        Envio savedEnvio = envioRepository.save(envio);
        notificacionService.notificarCreacionEnvio(savedEnvio); // Notificar al cliente
        return envioMapper.toDTO(savedEnvio);
    }

    @Override
    public EnvioDTO obtenerEnvioPorId(Long id) {
        Envio envio = envioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Envío no encontrado con ID: " + id));
        return envioMapper.toDTO(envio);
    }

    @Override
    public EnvioDTO obtenerEnvioPorOrdenId(Long ordenId) {
        Envio envio = envioRepository.findByOrdenId(ordenId)
                .orElseThrow(() -> new ResourceNotFoundException("Envío no encontrado para la Orden con ID: " + ordenId));
        return envioMapper.toDTO(envio);
    }

    @Override
    public EnvioDTO actualizarEstadoEnvio(Long id, EstadoEnvio nuevoEstado) {
        Envio envio = envioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Envío no encontrado con ID: " + id));

        if (envio.getEstado().equals(nuevoEstado)) {
            throw new BadRequestException("El envío ya se encuentra en el estado: " + nuevoEstado.name());
        }

        envio.setEstado(nuevoEstado);
        envio.setActualizacion(LocalDateTime.now());
        Envio updatedEnvio = envioRepository.save(envio);
        notificacionService.notificarCambioEstadoEnvio(updatedEnvio); // Notificar al cliente
        return envioMapper.toDTO(updatedEnvio);
    }

    @Override
    public EnvioDTO asignarCodigoRastreo(Long id, String codigoRastreo) {
        Envio envio = envioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Envío no encontrado con ID: " + id));

        if (envio.getCodigoRastreo() != null && !envio.getCodigoRastreo().isEmpty()) {
            throw new BadRequestException("El envío con ID " + id + " ya tiene un código de rastreo asignado.");
        }

        envio.setCodigoRastreo(codigoRastreo);
        envio.setActualizacion(LocalDateTime.now());
        Envio updatedEnvio = envioRepository.save(envio);
        // Opcional: notificar al cliente sobre el código de rastreo
        return envioMapper.toDTO(updatedEnvio);
    }

    @Override
    public List<EnvioDTO> listarTodosLosEnvios() {
        return envioMapper.toDTOList(envioRepository.findAll());
    }
}
