package com.ecommerce.backend.mapper;

import com.ecommerce.backend.dto.CarritoDTO;
import com.ecommerce.backend.dto.CarritoItemDTO;
import com.ecommerce.backend.model.Carrito;
import com.ecommerce.backend.model.CarritoItem;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class CarritoMapper {

    public CarritoDTO toDTO(Carrito carrito) {
        if (carrito == null) {
            return null;
        }
        return CarritoDTO.builder()
                .id(carrito.getId())
                .usuarioId(carrito.getUsuarioId())
                .items(toItemDTOList(carrito.getItems()))
                .total(carrito.getTotal())
                .creacion(carrito.getCreacion())
                .actualizacion(carrito.getActualizacion())
                .build();
    }

    public Carrito toEntity(CarritoDTO dto) {
        if (dto == null) {
            return null;
        }
        Carrito carrito = Carrito.builder()
                .id(dto.getId())
                .usuarioId(dto.getUsuarioId())
                .items(toItemEntityList(dto.getItems()))
                .build();
        return carrito;
    }

    public CarritoItemDTO toItemDTO(CarritoItem item) {
        if (item == null) {
            return null;
        }
        return CarritoItemDTO.builder()
                .productoId(item.getProductoId())
                .nombreProducto(item.getNombreProducto())
                .cantidad(item.getCantidad())
                .precioUnitario(item.getPrecioUnitario())
                .build();
    }

    public CarritoItem toItemEntity(CarritoItemDTO dto) {
        if (dto == null) {
            return null;
        }
        return CarritoItem.builder()
                .productoId(dto.getProductoId())
                .nombreProducto(dto.getNombreProducto())
                .cantidad(dto.getCantidad())
                .precioUnitario(dto.getPrecioUnitario())
                .build();
    }

    public List<CarritoItemDTO> toItemDTOList(List<CarritoItem> items) {
        if (items == null) {
            return null;
        }
        return items.stream()
                .map(this::toItemDTO)
                .collect(Collectors.toList());
    }

    public List<CarritoItem> toItemEntityList(List<CarritoItemDTO> dtos) {
        if (dtos == null) {
            return null;
        }
        return dtos.stream()
                .map(this::toItemEntity)
                .collect(Collectors.toList());
    }
}
