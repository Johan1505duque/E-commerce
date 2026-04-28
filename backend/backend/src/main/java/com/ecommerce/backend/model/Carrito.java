package com.ecommerce.backend.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(collection = "carritos")
public class Carrito {
    @Id
    private String id; // ID de MongoDB

    @Indexed(unique = true) // Un carrito por usuario
    private Long usuarioId;

    @Builder.Default
    private List<CarritoItem> items = new ArrayList<>();

    private BigDecimal total;

    @Builder.Default
    private LocalDateTime creacion = LocalDateTime.now();

    @Builder.Default
    private LocalDateTime actualizacion = LocalDateTime.now();

    // Método para calcular el total del carrito
    public void calcularTotal() {
        if (this.items == null) {
            this.items = new ArrayList<>();
        }
        this.total = items.stream()
                .map(item -> {
                    if (item.getPrecioUnitario() == null || item.getCantidad() == null) {
                        return BigDecimal.ZERO;
                    }
                    return item.getPrecioUnitario().multiply(BigDecimal.valueOf(item.getCantidad()));
                })
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
