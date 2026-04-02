package com.ecommerce.backend.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.ecommerce.backend.service.InventarioService;
import com.ecommerce.backend.dto.InventarioDTO;
import com.ecommerce.backend.dto.ApiResponse;

import java.util.List;

@RestController
@RequestMapping("/inventario")
@RequiredArgsConstructor
@Tag(name = "Inventario", description = "Gestión de inventario")
public class InventarioController {

    private final InventarioService inventarioService;

    @GetMapping
    @Operation(summary = "Listar todo el inventario")
    public ResponseEntity<ApiResponse<List<InventarioDTO>>> listarTodos() {
        return ResponseEntity.ok(
                ApiResponse.exito(
                        inventarioService.listarTodos(),
                        "Inventario obtenido correctamente"));
    }

    @GetMapping("/producto/{productoId}")
    @Operation(summary = "Buscar inventario por producto")
    public ResponseEntity<ApiResponse<InventarioDTO>> buscarPorProducto(
            @PathVariable Long productoId) {
        return ResponseEntity.ok(
                ApiResponse.exito(
                        inventarioService.buscarPorProducto(productoId),
                        "Inventario encontrado"));
    }

    @GetMapping("/stock-bajo")
    @Operation(summary = "Listar productos con stock bajo")
    public ResponseEntity<ApiResponse<List<InventarioDTO>>> stockBajo(
            @RequestParam(defaultValue = "5") Integer minimo) {
        return ResponseEntity.ok(
                ApiResponse.exito(
                        inventarioService.listarStockBajo(minimo),
                        "Productos con stock bajo"));
    }

    @PostMapping
    @Operation(summary = "Crear inventario para un producto")
    public ResponseEntity<ApiResponse<InventarioDTO>> guardar(
            @RequestParam Long productoId,
            @RequestParam Integer cantidad,
            @RequestParam(required = false) String ubicacion) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.creado(
                        inventarioService.guardar(productoId, cantidad, ubicacion),
                        "Inventario creado correctamente"));
    }

    @PutMapping("/producto/{productoId}")
    @Operation(summary = "Actualizar cantidad en inventario")
    public ResponseEntity<ApiResponse<InventarioDTO>> actualizarCantidad(
            @PathVariable Long productoId,
            @RequestParam Integer cantidad) {
        return ResponseEntity.ok(
                ApiResponse.exito(
                        inventarioService.actualizarCantidad(productoId, cantidad),
                        "Cantidad actualizada correctamente"));
    }
}
