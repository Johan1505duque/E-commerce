package com.ecommerce.backend.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CarritoDTO {
    private String id;

    @NotNull(message = "El ID del usuario no puede ser nulo")
    private Long usuarioId;

    private List<CarritoItemDTO> items;

    private BigDecimal total;

    private LocalDateTime creacion;
    private LocalDateTime actualizacion;
}
