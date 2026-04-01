package com.ecommerce.backend.service.impl;

import com.ecommerce.backend.dto.UsuarioDTO;
import com.ecommerce.backend.exception.BadRequestException;
import com.ecommerce.backend.exception.ResourceNotFoundException;
import com.ecommerce.backend.mapper.UsuarioMapper;
import com.ecommerce.backend.repository.UsuarioRepository;
import com.ecommerce.backend.service.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.ecommerce.backend.model.Usuario;


import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UsuarioServiceImpl implements UsuarioService {
    private final UsuarioRepository usuarioRepository;
    private final UsuarioMapper usuarioMapper;

    @Override
    public UsuarioDTO guardar(UsuarioDTO dto) {
        if (usuarioRepository.existsByCorreoElectronico(dto.getCorreoElectronico())) {
            throw new BadRequestException(
                    "El correo " + dto.getCorreoElectronico() + " ya está registrado");
        }
        Usuario usuario = usuarioMapper.toEntity(dto);
        usuario.setActivo(true);
        Usuario guardado = usuarioRepository.save(usuario);
        return usuarioMapper.toDTO(guardado);
    }

    @Override
    public UsuarioDTO buscarPorId(Long id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Usuario no encontrado con id: " + id));
        return usuarioMapper.toDTO(usuario);
    }

    @Override
    public UsuarioDTO buscarPorCorreo(String correo) {
        Usuario usuario = usuarioRepository
                .findByCorreoElectronicoAndActivo(correo, true)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Usuario no encontrado con correo: " + correo));
        return usuarioMapper.toDTO(usuario);
    }

    @Override
    public List<UsuarioDTO> listarActivos() {
        return usuarioMapper.toDTOList(
                usuarioRepository.findByActivo(true));
    }

    @Override
    public List<UsuarioDTO> listarTodos() {
        return usuarioMapper.toDTOList(usuarioRepository.findAll());
    }

    @Override
    public UsuarioDTO desactivar(Long id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Usuario no encontrado con id: " + id));
        usuario.setActivo(false);
        return usuarioMapper.toDTO(usuarioRepository.save(usuario));
    }

    @Override
    public boolean existeCorreo(String correo) {
        return usuarioRepository.existsByCorreoElectronico(correo);
    }
}
