package com.ecommerce.backend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CrearEnvioDTO {
    @NotNull(message = "El ID de la orden no puede ser nulo")
    private Long ordenId;

    @NotBlank(message = "La dirección de envío no puede estar vacía")
    private String direccionEnvio;
}
