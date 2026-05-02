package com.ecommerce.backend.controller.rest;

import com.ecommerce.backend.dto.ApiResponse;
import com.ecommerce.backend.dto.CrearEnvioDTO;
import com.ecommerce.backend.dto.EnvioDTO;
import com.ecommerce.backend.model.Enum.EstadoEnvio;
import com.ecommerce.backend.service.EnvioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/envios")
@RequiredArgsConstructor
@Tag(name = "Envios", description = "Gestión de envíos y notificaciones (REST)")
public class EnvioController {

    private final EnvioService envioService;

    @PostMapping
    @Operation(summary = "Crear un nuevo envío para una orden")
    public ResponseEntity<ApiResponse<EnvioDTO>> crearEnvio(@Valid @RequestBody CrearEnvioDTO dto) {
        EnvioDTO nuevoEnvio = envioService.crearEnvio(dto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.creado(nuevoEnvio, "Envío creado exitosamente"));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener envío por ID")
    public ResponseEntity<ApiResponse<EnvioDTO>> obtenerEnvioPorId(@PathVariable Long id) {
        EnvioDTO envio = envioService.obtenerEnvioPorId(id);
        return ResponseEntity.ok(ApiResponse.exito(envio, "Envío encontrado"));
    }

    @GetMapping("/orden/{ordenId}")
    @Operation(summary = "Obtener envío por ID de orden")
    public ResponseEntity<ApiResponse<EnvioDTO>> obtenerEnvioPorOrdenId(@PathVariable Long ordenId) {
        EnvioDTO envio = envioService.obtenerEnvioPorOrdenId(ordenId);
        return ResponseEntity.ok(ApiResponse.exito(envio, "Envío encontrado para la orden"));
    }

    @PutMapping("/{id}/estado")
    @Operation(summary = "Actualizar el estado de un envío")
    public ResponseEntity<ApiResponse<EnvioDTO>> actualizarEstadoEnvio(
            @PathVariable Long id,
            @RequestParam EstadoEnvio nuevoEstado) {
        EnvioDTO envioActualizado = envioService.actualizarEstadoEnvio(id, nuevoEstado);
        return ResponseEntity.ok(ApiResponse.exito(envioActualizado, "Estado del envío actualizado"));
    }

    @PutMapping("/{id}/rastreo")
    @Operation(summary = "Asignar código de rastreo a un envío")
    public ResponseEntity<ApiResponse<EnvioDTO>> asignarCodigoRastreo(
            @PathVariable Long id,
            @RequestParam String codigoRastreo) {
        EnvioDTO envioActualizado = envioService.asignarCodigoRastreo(id, codigoRastreo);
        return ResponseEntity.ok(ApiResponse.exito(envioActualizado, "Código de rastreo asignado"));
    }

    @GetMapping
    @Operation(summary = "Listar todos los envíos")
    public ResponseEntity<ApiResponse<List<EnvioDTO>>> listarTodosLosEnvios() {
        List<EnvioDTO> envios = envioService.listarTodosLosEnvios();
        return ResponseEntity.ok(ApiResponse.exito(envios, "Envíos listados correctamente"));
    }
}
