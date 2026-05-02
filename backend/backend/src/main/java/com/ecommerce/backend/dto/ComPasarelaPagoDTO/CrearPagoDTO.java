package com.ecommerce.backend.dto.ComPasarelaPagoDTO;

import lombok.AllArgsConstructor;
import lombok.Builder; // Importar Builder
import lombok.Data;
import lombok.NoArgsConstructor;
import com.ecommerce.backend.model.Enum.MedioPago;

import java.math.BigDecimal; // Asegúrate de que BigDecimal esté importado

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder // Añadir la anotación @Builder
public class CrearPagoDTO {
    private Long ordenId;
    private BigDecimal monto;
    private MedioPago medioPago;
}
