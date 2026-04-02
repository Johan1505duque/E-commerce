package com.ecommerce.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.ecommerce.backend.model.Enum.Rol;
import com.ecommerce.backend.model.Usuario;

import java.util.Optional;
import java.util.List;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    // Login — busca por correo
    Optional<Usuario> findByCorreoElectronico(String correoElectronico);

    // Login — busca por correo solo si está activo
    Optional<Usuario> findByCorreoElectronicoAndActivo(
            String correoElectronico, Boolean activo);

    // Registro — verifica si el correo ya existe
    boolean existsByCorreoElectronico(String correoElectronico);

    // Buscar usuarios por rol
    List<Usuario> findByRol(Rol rol);

    // Buscar solo usuarios activos
    List<Usuario> findByActivo(Boolean activo);
}
