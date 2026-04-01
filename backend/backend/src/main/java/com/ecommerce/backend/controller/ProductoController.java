package com.ecommerce.backend.controller;

import com.ecommerce.backend.dto.ApiResponse;
import com.ecommerce.backend.dto.ProductoDTO;
import com.ecommerce.backend.service.ProductoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/productos")
@RequiredArgsConstructor
@Tag(name = "Productos", description = "Gestión de productos")
public class ProductoController {
    private final ProductoService productoService;

    @GetMapping
    @Operation(summary = "Listar productos activos")
    public ResponseEntity<ApiResponse<List<ProductoDTO>>> listarActivos() {
        return ResponseEntity.ok(
                ApiResponse.exito(
                        productoService.listarActivos(),
                        "Productos obtenidos correctamente"));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar producto por ID")
    public ResponseEntity<ApiResponse<ProductoDTO>> buscarPorId(
            @PathVariable Long id) {
        return ResponseEntity.ok(
                ApiResponse.exito(
                        productoService.buscarPorId(id),
                        "Producto encontrado"));
    }

    @GetMapping("/buscar")
    @Operation(summary = "Buscar productos por nombre")
    public ResponseEntity<ApiResponse<List<ProductoDTO>>> buscarPorNombre(
            @RequestParam String nombre) {
        return ResponseEntity.ok(
                ApiResponse.exito(
                        productoService.buscarPorNombre(nombre),
                        "Búsqueda completada"));
    }

    @GetMapping("/precio")
    @Operation(summary = "Buscar productos por rango de precio")
    public ResponseEntity<ApiResponse<List<ProductoDTO>>> buscarPorPrecio(
            @RequestParam BigDecimal min,
            @RequestParam BigDecimal max) {
        return ResponseEntity.ok(
                ApiResponse.exito(
                        productoService.buscarPorRangoPrecio(min, max),
                        "Productos encontrados en ese rango"));
    }

    @PostMapping
    @Operation(summary = "Crear nuevo producto")
    public ResponseEntity<ApiResponse<ProductoDTO>> guardar(
            @RequestBody @Valid ProductoDTO dto) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.creado(
                        productoService.guardar(dto),
                        "Producto creado correctamente"));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar producto")
    public ResponseEntity<ApiResponse<ProductoDTO>> actualizar(
            @PathVariable Long id,
            @RequestBody @Valid ProductoDTO dto) {
        return ResponseEntity.ok(
                ApiResponse.exito(
                        productoService.actualizar(id, dto),
                        "Producto actualizado correctamente"));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Desactivar producto")
    public ResponseEntity<ApiResponse<ProductoDTO>> desactivar(
            @PathVariable Long id) {
        return ResponseEntity.ok(
                ApiResponse.exito(
                        productoService.desactivar(id),
                        "Producto desactivado correctamente"));
    }
}
