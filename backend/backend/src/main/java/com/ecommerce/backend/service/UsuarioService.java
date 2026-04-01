package com.ecommerce.backend.service;

import com.ecommerce.backend.model.Usuario;
import java.util.List;
import java.util.Optional;


public interface UsuarioService {
    Usuario guardar(Usuario usuario);
    Optional<Usuario> buscarPorCorreo(String correo);
    List<Usuario> listarTodos();
    List<Usuario> listarActivos();
    Optional<Usuario> buscarPorId(Long id);
    void desactivar(Long id);
}
