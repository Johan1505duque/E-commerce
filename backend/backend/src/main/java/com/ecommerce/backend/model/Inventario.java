package com.ecommerce.backend.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDateTime;

@Document(collection = "inventarios")
@Data
@Builder
@AllArgsConstructor
public class Inventario {

    @Id
    private String id;

    @Indexed(unique = true)
    @Field("productoId")
    private Long productoId;

    private Integer cantidad;

    private String ubicacionBodega;

    @Builder.Default
    private LocalDateTime creacion = LocalDateTime.now();

    @Builder.Default
    private LocalDateTime actualizacion = LocalDateTime.now();

    public Inventario() {
        this.creacion = LocalDateTime.now();
        this.actualizacion = LocalDateTime.now();
    }

    public Inventario(Long productoId, Integer cantidad, String ubicacionBodega) {
        this();
        this.productoId = productoId;
        this.cantidad = cantidad;
        this.ubicacionBodega = ubicacionBodega;
    }
}
