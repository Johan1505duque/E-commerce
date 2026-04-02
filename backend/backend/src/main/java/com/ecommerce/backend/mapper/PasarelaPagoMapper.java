package com.ecommerce.backend.mapper;

import org.springframework.stereotype.Component;

import com.ecommerce.backend.dto.ComPasarelaPagoDTO.PasarelaPagoDTO;
import com.ecommerce.backend.model.PasarelaPago;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class PasarelaPagoMapper {
    public PasarelaPagoDTO toDTO(PasarelaPago pago) {
        return PasarelaPagoDTO.builder()
                .id(pago.getId())
                .ordenId(pago.getOrden().getId())
                .monto(pago.getMonto())
                .medioPago(pago.getMedioPago())
                .estado(pago.getEstado())
                .codigoTransaccion(pago.getCodigoTransaccion())
                .fecha(pago.getFecha())
                .build();
    }

    public List<PasarelaPagoDTO> toDTOList(List<PasarelaPago> pagos) {
        return pagos.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }
}
