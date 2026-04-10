package com.ecommerce.backend.controller;

import com.ecommerce.backend.dto.InventarioDTO;
import com.ecommerce.backend.service.InventarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/inventarios")
@RequiredArgsConstructor
@Tag(name = "Inventarios", description = "Operaciones de inventario gestionadas en MongoDB")
public class InventarioController {

    private final InventarioService inventarioService;

    @PostMapping
    @Operation(summary = "Crear un nuevo registro de inventario")
    public ResponseEntity<InventarioDTO> guardar(@Valid @RequestBody InventarioDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(inventarioService.guardar(dto.getProductoId(), dto.getCantidad(), dto.getUbicacionBodega()));
    }

    @GetMapping("/producto/{productoId}")
    @Operation(summary = "Buscar inventario por ID de producto")
    public ResponseEntity<InventarioDTO> buscarPorProducto(@PathVariable Long productoId) {
        return ResponseEntity.ok(inventarioService.buscarPorProducto(productoId));
    }

    @PutMapping("/producto/{productoId}")
    @Operation(summary = "Actualizar la cantidad de inventario de un producto")
    public ResponseEntity<InventarioDTO> actualizarCantidad(@PathVariable Long productoId, @RequestParam Integer cantidad) {
        return ResponseEntity.ok(inventarioService.actualizarCantidad(productoId, cantidad));
    }

    @GetMapping
    @Operation(summary = "Listar todos los registros de inventario")
    public ResponseEntity<List<InventarioDTO>> listarTodos() {
        return ResponseEntity.ok(inventarioService.listarTodos());
    }

    @GetMapping("/stock-bajo")
    @Operation(summary = "Listar productos con stock por debajo del mínimo")
    public ResponseEntity<List<InventarioDTO>> listarStockBajo(@RequestParam(defaultValue = "5") Integer minimo) {
        return ResponseEntity.ok(inventarioService.listarStockBajo(minimo));
    }

    @GetMapping("/producto/{productoId}/tiene-stock")
    @Operation(summary = "Verificar si un producto tiene stock disponible")
    public ResponseEntity<Boolean> tieneStock(@PathVariable Long productoId) {
        return ResponseEntity.ok(inventarioService.tieneStock(productoId));
    }
}
