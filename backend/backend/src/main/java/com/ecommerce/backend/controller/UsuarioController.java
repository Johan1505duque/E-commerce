package com.ecommerce.backend.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.*;

import com.ecommerce.backend.model.Usuario;
import com.ecommerce.backend.service.UsuarioService;
import com.ecommerce.backend.mapper.UsuarioMapper;
import com.ecommerce.backend.dto.UsuarioDTO;
import com.ecommerce.backend.exception.*;

import java.util.List;

@RestController
@RequestMapping("/usuarios")
@RequiredArgsConstructor
@Tag(name = "Usuarios", description = "Gestión de usuarios")
public class UsuarioController {

    private final UsuarioService usuarioService;
    private final UsuarioMapper usuarioMapper;

    @GetMapping
    @Operation(summary = "Listar usuarios activos")
    public ResponseEntity<List<UsuarioDTO>> listarActivos() {
        return ResponseEntity.ok(
                usuarioMapper.toDTOList(usuarioService.listarActivos()));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar usuario por ID")
    public ResponseEntity<UsuarioDTO> buscarPorId(@PathVariable Long id) {
        Usuario usuario = usuarioService.buscarPorId(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Usuario no encontrado con id: " + id));
        return ResponseEntity.ok(usuarioMapper.toDTO(usuario));
    }

    @PostMapping
    @Operation(summary = "Crear nuevo usuario")
    public ResponseEntity<UsuarioDTO> guardar(
            @RequestBody @Valid UsuarioDTO dto) {
        if (usuarioService.existeCorreo(dto.getCorreoElectronico())) {
            throw new BadRequestException("El correo ya está registrado");
        }
        Usuario usuario = usuarioMapper.toEntity(dto);
        usuario.setActivo(true);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(usuarioMapper.toDTO(usuarioService.guardar(usuario)));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Desactivar usuario")
    public ResponseEntity<Void> desactivar(@PathVariable Long id) {
        usuarioService.buscarPorId(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Usuario no encontrado con id: " + id));
        usuarioService.desactivar(id);
        return ResponseEntity.noContent().build();
    }
}
