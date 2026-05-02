package com.ecommerce.backend.dto;

import com.ecommerce.backend.model.Enum.EstadoEnvio;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EnvioDTO {
    private Long id;

    @NotNull(message = "El ID de la orden no puede ser nulo")
    private Long ordenId;

    @NotBlank(message = "La dirección de envío no puede estar vacía")
    private String direccionEnvio;

    private EstadoEnvio estado;

    private String codigoRastreo;

    private LocalDateTime creacion;
    private LocalDateTime actualizacion;
}
