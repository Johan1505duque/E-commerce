package com.ecommerce.backend.mapper;

import org.springframework.stereotype.Component;

import com.ecommerce.backend.dto.UsuarioDTO;
import com.ecommerce.backend.model.Usuario;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class UsuarioMapper {

    public UsuarioDTO toDTO(Usuario usuario) {
        return UsuarioDTO.builder()
                .id(usuario.getId())
                .nombre(usuario.getNombre())
                .apellido(usuario.getApellido())
                .correoElectronico(usuario.getCorreoElectronico())
                .rol(usuario.getRol())
                .activo(usuario.getActivo())
                .creacion(usuario.getCreacion())
                .build();
    }

    public Usuario toEntity(UsuarioDTO dto) {
        return Usuario.builder()
                .id(dto.getId())
                .nombre(dto.getNombre())
                .apellido(dto.getApellido())
                .correoElectronico(dto.getCorreoElectronico())
                .rol(dto.getRol())
                .activo(dto.getActivo())
                .build();
    }

    public List<UsuarioDTO> toDTOList(List<Usuario> usuarios) {
        return usuarios.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }
}
