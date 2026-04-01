package com.ecommerce.backend.service;

import com.ecommerce.backend.dto.UsuarioDTO;

import java.util.List;



public interface UsuarioService {
    UsuarioDTO guardar(UsuarioDTO dto);
    UsuarioDTO buscarPorId(Long id);
    UsuarioDTO buscarPorCorreo(String correo);
    List<UsuarioDTO> listarActivos();
    List<UsuarioDTO> listarTodos();
    UsuarioDTO desactivar(Long id);
    boolean existeCorreo(String correo);
}
