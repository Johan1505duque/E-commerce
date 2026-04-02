package com.ecommerce.backend.dto.ComPasarelaPagoDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.ecommerce.backend.model.Enum.MedioPago;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CrearPagoDTO {
    private Long ordenId;
    private MedioPago medioPago;
}
