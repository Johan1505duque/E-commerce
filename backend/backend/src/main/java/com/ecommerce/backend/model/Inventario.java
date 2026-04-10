package com.ecommerce.backend.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDateTime;

@Document(collection = "inventarios")
public class Inventario {

    @Id
    private String id;

    @Indexed(unique = true)
    @Field("productoId")
    private Long productoId;

    private Integer cantidad;

    private String ubicacionBodega;

    private LocalDateTime creacion;

    private LocalDateTime actualizacion;

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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Long getProductoId() {
        return productoId;
    }

    public void setProductoId(Long productoId) {
        this.productoId = productoId;
    }

    public Integer getCantidad() {
        return cantidad;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }

    public String getUbicacionBodega() {
        return ubicacionBodega;
    }

    public void setUbicacionBodega(String ubicacionBodega) {
        this.ubicacionBodega = ubicacionBodega;
    }

    public LocalDateTime getCreacion() {
        return creacion;
    }

    public void setCreacion(LocalDateTime creacion) {
        this.creacion = creacion;
    }

    public LocalDateTime getActualizacion() {
        return actualizacion;
    }

    public void setActualizacion(LocalDateTime actualizacion) {
        this.actualizacion = actualizacion;
    }

    @Override
    public String toString() {
        return "Inventario{" +
               "id='" + id + '\'' +
               ", productoId=" + productoId +
               ", cantidad=" + cantidad +
               ", ubicacionBodega='" + ubicacionBodega + '\'' +
               ", creacion=" + creacion +
               ", actualizacion=" + actualizacion +
               '}';
    }
}
