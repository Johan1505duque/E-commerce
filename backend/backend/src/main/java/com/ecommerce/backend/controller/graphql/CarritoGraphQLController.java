package com.ecommerce.backend.controller.graphql;

import com.ecommerce.backend.dto.CarritoDTO;
import com.ecommerce.backend.service.CarritoService;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class CarritoGraphQLController {

    private final CarritoService carritoService;

    @QueryMapping
    public CarritoDTO obtenerCarrito(@Argument Long usuarioId) {
        return carritoService.obtenerCarritoPorUsuario(usuarioId);
    }

    @MutationMapping
    public CarritoDTO agregarOActualizarProducto(@Argument Long usuarioId, @Argument Long productoId, @Argument Integer cantidad) {
        return carritoService.agregarOActualizarProducto(usuarioId, productoId, cantidad);
    }

    @MutationMapping
    public CarritoDTO eliminarProducto(@Argument Long usuarioId, @Argument Long productoId) {
        return carritoService.eliminarProducto(usuarioId, productoId);
    }

    @MutationMapping
    public CarritoDTO limpiarCarrito(@Argument Long usuarioId) {
        carritoService.limpiarCarrito(usuarioId);
        return carritoService.obtenerCarritoPorUsuario(usuarioId);
    }

    @MutationMapping
    public Boolean eliminarCarrito(@Argument Long usuarioId) {
        carritoService.eliminarCarrito(usuarioId);
        return true;
    }
}
