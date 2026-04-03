package com.ecommerce.backend.model.ComOrdenProducto;

import com.ecommerce.backend.model.Orden;
import com.ecommerce.backend.model.Producto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "orden_producto")
public class OrdenProducto {

    @EmbeddedId
    private OrdenProductoId id;

    @ManyToOne
    @MapsId("ordenId")
    @JoinColumn(name = "id_orden")
    private Orden orden;

    @ManyToOne
    @MapsId("productoId")
    @JoinColumn(name = "id_producto")
    private Producto producto;

    @Column(name = "cantidad", nullable = false)
    private Integer cantidad;

    @Column(name = "precio_unitario", nullable = false,
            precision = 10, scale = 2)
    private BigDecimal precioUnitario;

    @CreationTimestamp
    @Column(name = "creacion", nullable = false, updatable = false)
    private LocalDateTime creacion;
}
