package com.ecommerce.backend.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CarritoItem {
    private Long productoId;
    private String nombreProducto; // Para mostrar en el carrito sin consultar siempre PostgreSQL
    private Integer cantidad;
    private BigDecimal precioUnitario;
}
