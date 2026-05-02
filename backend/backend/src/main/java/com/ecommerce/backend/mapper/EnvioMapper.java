package com.ecommerce.backend.mapper;

import com.ecommerce.backend.dto.CrearEnvioDTO;
import com.ecommerce.backend.dto.EnvioDTO;
import com.ecommerce.backend.model.Envio;
import com.ecommerce.backend.model.Orden;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class EnvioMapper {

    public EnvioDTO toDTO(Envio envio) {
        if (envio == null) {
            return null;
        }
        return EnvioDTO.builder()
                .id(envio.getId())
                .ordenId(envio.getOrden() != null ? envio.getOrden().getId() : null)
                .direccionEnvio(envio.getDireccionEnvio())
                .estado(envio.getEstado())
                .codigoRastreo(envio.getCodigoRastreo())
                .creacion(envio.getCreacion())
                .actualizacion(envio.getActualizacion())
                .build();
    }

    public Envio toEntity(CrearEnvioDTO dto) {
        if (dto == null) {
            return null;
        }
        // La orden se setea en el servicio
        return Envio.builder()
                .direccionEnvio(dto.getDireccionEnvio())
                .build();
    }

    public List<EnvioDTO> toDTOList(List<Envio> envios) {
        if (envios == null) {
            return null;
        }
        return envios.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public void updateEntityFromDto(EnvioDTO dto, Envio envio) {
        if (dto == null || envio == null) {
            return;
        }
        if (dto.getDireccionEnvio() != null) {
            envio.setDireccionEnvio(dto.getDireccionEnvio());
        }
        if (dto.getEstado() != null) {
            envio.setEstado(dto.getEstado());
        }
        if (dto.getCodigoRastreo() != null) {
            envio.setCodigoRastreo(dto.getCodigoRastreo());
        }
        // No se actualiza ordenId directamente desde el DTO en este método
    }
}
