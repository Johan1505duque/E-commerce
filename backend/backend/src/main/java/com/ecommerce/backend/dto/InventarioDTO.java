package com.ecommerce.backend.dto;

import lombok.*;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InventarioDTO {
    private Long id;
    private Long productoId;
    private String productoNombre;
    private Integer cantidad;
    private String ubicacionBodega;
    private LocalDateTime actualizacion;
}
