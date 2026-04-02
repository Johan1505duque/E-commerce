package com.ecommerce.backend.dto.ComPasarelaPagoDTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import com.ecommerce.backend.model.Enum.MedioPago;
import com.ecommerce.backend.model.Enum.EstadoPago;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor

public class PasarelaPagoDTO {
    private Long id;
    private Long ordenId;
    private BigDecimal monto;
    private MedioPago medioPago;
    private EstadoPago estado;
    private String codigoTransaccion;
    private LocalDateTime fecha;
}
