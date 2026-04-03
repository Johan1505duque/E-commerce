package com.ecommerce.backend.model.ComOrdenProducto;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrdenProductoId implements Serializable {
    @Column(name = "id_orden")
    private Long ordenId;

    @Column(name = "id_producto")
    private Long productoId;
}
