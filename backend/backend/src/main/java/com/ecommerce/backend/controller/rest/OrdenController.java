package com.ecommerce.backend.controller.rest;

import com.ecommerce.backend.dto.ApiResponse;
import com.ecommerce.backend.dto.ComOrdenDTO.CrearOrdenDTO;
import com.ecommerce.backend.model.Enum.EstadoOrden;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.ecommerce.backend.service.OrdenService;
import com.ecommerce.backend.dto.ComOrdenDTO.OrdenDTO;

import java.util.List;


@RestController
@RequestMapping("/ordenes")
@RequiredArgsConstructor
@Tag(name = "Ordenes", description = "Gestión de órdenes")
public class OrdenController {

    private final OrdenService ordenService;

    @PostMapping
    @Operation(summary = "Crear nueva orden")
    public ResponseEntity<ApiResponse<OrdenDTO>> crearOrden(
            @RequestBody @Valid CrearOrdenDTO dto) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.creado(
                        ordenService.crearOrden(dto),
                        "Orden creada correctamente"));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar orden por ID")
    public ResponseEntity<ApiResponse<OrdenDTO>> buscarPorId(
            @PathVariable Long id) {
        return ResponseEntity.ok(
                ApiResponse.exito(
                        ordenService.buscarPorId(id),
                        "Orden encontrada"));
    }

    @GetMapping("/usuario/{usuarioId}")
    @Operation(summary = "Listar órdenes por usuario")
    public ResponseEntity<ApiResponse<List<OrdenDTO>>> listarPorUsuario(
            @PathVariable Long usuarioId) {
        return ResponseEntity.ok(
                ApiResponse.exito(
                        ordenService.listarPorUsuario(usuarioId),
                        "Órdenes obtenidas correctamente"));
    }

    @GetMapping("/estado/{estado}")
    @Operation(summary = "Listar órdenes por estado")
    public ResponseEntity<ApiResponse<List<OrdenDTO>>> listarPorEstado(
            @PathVariable EstadoOrden estado) {
        return ResponseEntity.ok(
                ApiResponse.exito(
                        ordenService.listarPorEstado(estado),
                        "Órdenes obtenidas correctamente"));
    }

    @PutMapping("/{id}/estado")
    @Operation(summary = "Cambiar estado de la orden")
    public ResponseEntity<ApiResponse<OrdenDTO>> cambiarEstado(
            @PathVariable Long id,
            @RequestParam EstadoOrden estado) {
        return ResponseEntity.ok(
                ApiResponse.exito(
                        ordenService.cambiarEstado(id, estado),
                        "Estado actualizado correctamente"));
    }

    @PutMapping("/{id}/cancelar")
    @Operation(summary = "Cancelar orden")
    public ResponseEntity<ApiResponse<OrdenDTO>> cancelar(
            @PathVariable Long id) {
        return ResponseEntity.ok(
                ApiResponse.exito(
                        ordenService.cancelarOrden(id),
                        "Orden cancelada correctamente"));
    }
}
