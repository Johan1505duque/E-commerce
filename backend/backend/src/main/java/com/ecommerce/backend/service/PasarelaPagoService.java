package com.ecommerce.backend.service;

import com.ecommerce.backend.dto.ComPasarelaPagoDTO.*;
import com.ecommerce.backend.model.Enum.EstadoPago;

import java.util.List;

public interface PasarelaPagoService {

    PasarelaPagoDTO procesarPago(CrearPagoDTO dto);
    PasarelaPagoDTO buscarPorOrden(Long ordenId);
    PasarelaPagoDTO buscarPorId(Long id);
    List<PasarelaPagoDTO> listarPorEstado(EstadoPago estado);
    PasarelaPagoDTO confirmarPago(Long id);
    PasarelaPagoDTO rechazarPago(Long id);

}
