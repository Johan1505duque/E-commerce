package com.ecommerce.backend.controller.rest;

import com.ecommerce.backend.dto.ApiResponse;
import com.ecommerce.backend.dto.ComPasarelaPagoDTO.CrearPagoDTO;
import com.ecommerce.backend.dto.ComPasarelaPagoDTO.PasarelaPagoDTO;
import com.ecommerce.backend.model.Enum.EstadoPago;
import com.ecommerce.backend.service.PasarelaPagoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/pagos")
@RequiredArgsConstructor
@Tag(name = "Pagos", description = "Gestión de pagos")
public class PasarelaPagoController {

    private final PasarelaPagoService pasarelaPagoService;

    @PostMapping
    @Operation(summary = "Procesar pago de una orden")
    public ResponseEntity<ApiResponse<PasarelaPagoDTO>> procesarPago(
            @RequestBody @Valid CrearPagoDTO dto) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.creado(
                        pasarelaPagoService.procesarPago(dto),
                        "Pago procesado correctamente"));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar pago por ID")
    public ResponseEntity<ApiResponse<PasarelaPagoDTO>> buscarPorId(
            @PathVariable Long id) {
        return ResponseEntity.ok(
                ApiResponse.exito(
                        pasarelaPagoService.buscarPorId(id),
                        "Pago encontrado"));
    }

    @GetMapping("/orden/{ordenId}")
    @Operation(summary = "Buscar pago por orden")
    public ResponseEntity<ApiResponse<PasarelaPagoDTO>> buscarPorOrden(
            @PathVariable Long ordenId) {
        return ResponseEntity.ok(
                ApiResponse.exito(
                        pasarelaPagoService.buscarPorOrden(ordenId),
                        "Pago encontrado"));
    }

    @GetMapping("/estado/{estado}")
    @Operation(summary = "Listar pagos por estado")
    public ResponseEntity<ApiResponse<List<PasarelaPagoDTO>>> listarPorEstado(
            @PathVariable EstadoPago estado) {
        return ResponseEntity.ok(
                ApiResponse.exito(
                        pasarelaPagoService.listarPorEstado(estado),
                        "Pagos obtenidos correctamente"));
    }

    @PutMapping("/{id}/confirmar")
    @Operation(summary = "Confirmar pago")
    public ResponseEntity<ApiResponse<PasarelaPagoDTO>> confirmar(
            @PathVariable Long id) {
        return ResponseEntity.ok(
                ApiResponse.exito(
                        pasarelaPagoService.confirmarPago(id),
                        "Pago confirmado correctamente"));
    }

    @PutMapping("/{id}/rechazar")
    @Operation(summary = "Rechazar pago")
    public ResponseEntity<ApiResponse<PasarelaPagoDTO>> rechazar(
            @PathVariable Long id) {
        return ResponseEntity.ok(
                ApiResponse.exito(
                        pasarelaPagoService.rechazarPago(id),
                        "Pago rechazado"));
    }
}
