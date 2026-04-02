package com.ecommerce.backend.dto;

import com.ecommerce.backend.dto.ComOrdenDTO.OrdenProductoDTO;
import com.ecommerce.backend.model.Enum.EstadoOrden;
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
public class OrdenDTO {
    private Long id;
    private Long usuarioId;
    private String usuarioNombre;
    private List<OrdenProductoDTO> productos;
    private BigDecimal total;
    private BigDecimal envio;
    private EstadoOrden estado;
    private LocalDateTime creacion;
}
