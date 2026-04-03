package com.ecommerce.backend.service.impl;

import com.ecommerce.backend.model.ComOrdenProducto.OrdenProducto;
import com.ecommerce.backend.model.ComOrdenProducto.OrdenProductoId;
import com.ecommerce.backend.model.Enum.EstadoOrden;
import com.ecommerce.backend.service.OrdenService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import com.ecommerce.backend.repository.*;
import com.ecommerce.backend.mapper.OrdenMapper;
import com.ecommerce.backend.dto.ComOrdenDTO.OrdenDTO;
import com.ecommerce.backend.dto.ComOrdenDTO.*;
import com.ecommerce.backend.model.*;
import com.ecommerce.backend.exception.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrdenServiceImpl implements OrdenService {

    private final OrdenRepository ordenRepository;
    private final UsuarioRepository usuarioRepository;
    private final ProductoRepository productoRepository;
    private final InventarioRepository inventarioRepository;
    private final OrdenMapper ordenMapper;

    @Override
    @Transactional
    public OrdenDTO crearOrden(CrearOrdenDTO dto) {
        // 1. Verificar usuario
        Usuario usuario = usuarioRepository.findById(dto.getUsuarioId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Usuario no encontrado con id: " + dto.getUsuarioId()));

        // 2. Crear la orden base
        Orden orden = Orden.builder()
                .usuario(usuario)
                .envio(dto.getEnvio() != null ?
                        dto.getEnvio() : BigDecimal.ZERO)
                .estado(EstadoOrden.PENDIENTE)
                .total(BigDecimal.ZERO)
                .build();

        // 3. Procesar cada item
        List<OrdenProducto> items = new ArrayList<>();
        BigDecimal total = BigDecimal.ZERO;

        for (ItemOrdenDTO item : dto.getItems()) {
            // Verificar producto
            Producto producto = productoRepository
                    .findByIdAndActivo(item.getProductoId(), true)
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "Producto no encontrado: " + item.getProductoId()));

            // Verificar stock
            Inventario inventario = inventarioRepository
                    .findByProductoId(item.getProductoId())
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "Sin inventario para: " + producto.getNombre()));

            if (inventario.getCantidad() < item.getCantidad()) {
                throw new BadRequestException(
                        "Stock insuficiente para: " + producto.getNombre()
                                + ". Disponible: " + inventario.getCantidad());
            }

            // Descontar stock
            inventario.setCantidad(
                    inventario.getCantidad() - item.getCantidad());
            inventarioRepository.save(inventario);

            // Crear item de orden
            OrdenProducto op = OrdenProducto.builder()
                    .id(new OrdenProductoId())
                    .orden(orden)
                    .producto(producto)
                    .cantidad(item.getCantidad())
                    .precioUnitario(producto.getPrecio())
                    .build();
            items.add(op);

            // Acumular total
            total = total.add(producto.getPrecio()
                    .multiply(BigDecimal.valueOf(item.getCantidad())));
        }

        orden.setProductos(items);
        orden.setTotal(total.add(orden.getEnvio()));
        return ordenMapper.toDTO(ordenRepository.save(orden));
    }

    @Override
    @Transactional
    public OrdenDTO buscarPorId(Long id) {
        return ordenMapper.toDTO(
                ordenRepository.findById(id)
                        .orElseThrow(() -> new ResourceNotFoundException(
                                "Orden no encontrada con id: " + id)));
    }

    @Override
    @Transactional
    public List<OrdenDTO> listarPorUsuario(Long usuarioId) {
        return ordenMapper.toDTOList(
                ordenRepository.findByUsuarioId(usuarioId));
    }

    @Override
    @Transactional
    public List<OrdenDTO> listarPorEstado(EstadoOrden estado) {
        return ordenMapper.toDTOList(
                ordenRepository.findByEstado(estado));
    }

    @Override
    @Transactional
    public OrdenDTO cambiarEstado(Long id, EstadoOrden estado) {
        Orden orden = ordenRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Orden no encontrada con id: " + id));
        orden.setEstado(estado);
        return ordenMapper.toDTO(ordenRepository.save(orden));
    }

    @Override
    @Transactional
    public OrdenDTO cancelarOrden(Long id) {
        Orden orden = ordenRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Orden no encontrada con id: " + id));

        if (orden.getEstado() == EstadoOrden.ENTREGADO) {
            throw new BadRequestException(
                    "No se puede cancelar una orden ya entregada");
        }

        // Devolver stock
        for (OrdenProducto op : orden.getProductos()) {
            inventarioRepository.findByProductoId(op.getProducto().getId())
                    .ifPresent(inv -> {
                        inv.setCantidad(inv.getCantidad() + op.getCantidad());
                        inventarioRepository.save(inv);
                    });
        }

        orden.setEstado(EstadoOrden.CANCELADO);
        return ordenMapper.toDTO(ordenRepository.save(orden));
    }
}
