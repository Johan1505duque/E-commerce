package com.ecommerce.backend.dto.ComOrdenDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ItemOrdenDTO {
    private Long productoId;
    private Integer cantidad;
}
