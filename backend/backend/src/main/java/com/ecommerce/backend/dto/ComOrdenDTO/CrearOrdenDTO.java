package com.ecommerce.backend.dto.ComOrdenDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CrearOrdenDTO {
    private Long usuarioId;
    private List<ItemOrdenDTO> items;
    private BigDecimal envio;
}
