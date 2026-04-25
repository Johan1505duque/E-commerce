package com.ecommerce.backend.controller.rest;

import com.ecommerce.backend.dto.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;

import com.ecommerce.backend.service.UsuarioService;
import com.ecommerce.backend.dto.UsuarioDTO;

import java.util.List;

@RestController
@RequestMapping("/usuarios")
@RequiredArgsConstructor
@Tag(name = "Usuarios", description = "Gestión de usuarios")
public class UsuarioController {

    private final UsuarioService usuarioService;

    @GetMapping
    @Operation(summary = "Listar usuarios activos")
    public ResponseEntity<ApiResponse<List<UsuarioDTO>>> listarActivos() {
        return ResponseEntity.ok(
                ApiResponse.exito(
                        usuarioService.listarActivos(),
                        "Usuarios obtenidos correctamente"));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar usuario por ID")
    public ResponseEntity<ApiResponse<UsuarioDTO>> buscarPorId(
            @PathVariable Long id) {
        return ResponseEntity.ok(
                ApiResponse.exito(
                        usuarioService.buscarPorId(id),
                        "Usuario encontrado"));
    }

    @PostMapping
    @Operation(summary = "Crear nuevo usuario")
    public ResponseEntity<ApiResponse<UsuarioDTO>> guardar(
            @RequestBody @Valid UsuarioDTO dto) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.creado(
                        usuarioService.guardar(dto),
                        "Usuario creado correctamente"));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Desactivar usuario")
    public ResponseEntity<ApiResponse<UsuarioDTO>> desactivar(
            @PathVariable Long id) {
        return ResponseEntity.ok(
                ApiResponse.exito(
                        usuarioService.desactivar(id),
                        "Usuario desactivado correctamente"));
    }
}
