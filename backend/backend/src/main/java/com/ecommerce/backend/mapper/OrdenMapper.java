package com.ecommerce.backend.mapper;

import com.ecommerce.backend.dto.ComOrdenDTO.OrdenProductoDTO;
import com.ecommerce.backend.dto.ComOrdenDTO.OrdenDTO;
import com.ecommerce.backend.model.Orden;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class OrdenMapper {
    public OrdenDTO toDTO(Orden orden) {
        List<OrdenProductoDTO> productos = orden.getProductos()
                .stream()
                .map(op -> OrdenProductoDTO.builder()
                        .productoId(op.getProducto().getId())
                        .productoNombre(op.getProducto().getNombre())
                        .cantidad(op.getCantidad())
                        .precioUnitario(op.getPrecioUnitario())
                        .subtotal(op.getPrecioUnitario()
                                .multiply(BigDecimal.valueOf(op.getCantidad())))
                        .build())
                .collect(Collectors.toList());

        return OrdenDTO.builder()
                .id(orden.getId())
                .usuarioId(orden.getUsuario().getId())
                .usuarioNombre(orden.getUsuario().getNombre()
                        + " " + orden.getUsuario().getApellido())
                .productos(productos)
                .total(orden.getTotal())
                .envio(orden.getEnvio())
                .estado(orden.getEstado())
                .creacion(orden.getCreacion())
                .build();
    }

    public List<OrdenDTO> toDTOList(List<Orden> ordenes) {
        return ordenes.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }
}
