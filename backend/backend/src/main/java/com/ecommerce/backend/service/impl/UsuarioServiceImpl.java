package com.ecommerce.backend.service.impl;

import com.ecommerce.backend.dto.UsuarioDTO;
import com.ecommerce.backend.exception.BadRequestException;
import com.ecommerce.backend.exception.ResourceNotFoundException;
import com.ecommerce.backend.mapper.UsuarioMapper;
import com.ecommerce.backend.repository.UsuarioRepository;
import com.ecommerce.backend.service.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.ecommerce.backend.model.Usuario;


import java.util.List;

@Service
@RequiredArgsConstructor
public class UsuarioServiceImpl implements UsuarioService {
    private final UsuarioRepository usuarioRepository;
    private final UsuarioMapper usuarioMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UsuarioDTO guardar(UsuarioDTO dto) {
        if (usuarioRepository.existsByCorreoElectronico(dto.getCorreoElectronico())) {
            throw new BadRequestException(
                    "El correo " + dto.getCorreoElectronico() + " ya está registrado");
        }
        Usuario usuario = usuarioMapper.toEntity(dto);
        
        // Codificar la contraseña antes de guardar
        usuario.setPassword(passwordEncoder.encode(dto.getPassword()));
        
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
    public UsuarioDTO actualizar(Long id, UsuarioDTO dto) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Usuario no encontrado con id: " + id));
        
        // Actualizar campos
        usuario.setNombre(dto.getNombre());
        usuario.setApellido(dto.getApellido());
        
        // Verificar si se cambia el correo y si el nuevo ya existe
        if (!usuario.getCorreoElectronico().equals(dto.getCorreoElectronico()) &&
            usuarioRepository.existsByCorreoElectronico(dto.getCorreoElectronico())) {
            throw new BadRequestException("El correo " + dto.getCorreoElectronico() + " ya está registrado por otro usuario");
        }
        usuario.setCorreoElectronico(dto.getCorreoElectronico());
        
        // Codificar nueva contraseña si se proporciona
        if (dto.getPassword() != null && !dto.getPassword().isBlank()) {
            usuario.setPassword(passwordEncoder.encode(dto.getPassword()));
        }
        
        if (dto.getRol() != null) {
            usuario.setRol(dto.getRol());
        }

        return usuarioMapper.toDTO(usuarioRepository.save(usuario));
    }

    @Override
    public boolean existeCorreo(String correo) {
        return usuarioRepository.existsByCorreoElectronico(correo);
    }
}
