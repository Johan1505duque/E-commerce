package com.ecommerce.backend.dto.ComOrdenDTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder // Añadimos @Builder para facilitar la creación en tests
public class CrearOrdenDTO {
    @NotNull(message = "El ID de usuario no puede ser nulo")
    private Long usuarioId;

    @NotNull(message = "La orden debe contener al menos un item")
    private List<ItemOrdenDTO> items;

    private BigDecimal envio;

    @NotBlank(message = "La dirección de envío no puede estar vacía")
    private String direccionEnvio; // Nuevo campo para la dirección de envío
}
