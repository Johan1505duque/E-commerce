package com.ecommerce.backend.controller.graphql;

import com.ecommerce.backend.dto.InventarioDTO;
import com.ecommerce.backend.service.InventarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class InventarioGraphQLController {

    private final InventarioService inventarioService;

    // --- Consultas ---

    @QueryMapping
    public List<InventarioDTO> listarTodoInventario() {
        return inventarioService.listarTodos();
    }

    @QueryMapping
    public InventarioDTO buscarInventarioPorProducto(@Argument Long productoId) {
        return inventarioService.buscarPorProducto(productoId);
    }

    @QueryMapping
    public List<InventarioDTO> listarStockBajo(@Argument Integer minimo) {
        return inventarioService.listarStockBajo(minimo != null ? minimo : 5);
    }

    // --- Mutaciones ---

    @MutationMapping
    public InventarioDTO guardarInventario(@Argument Long productoId, @Argument Integer cantidad, @Argument String ubicacion) {
        return inventarioService.guardar(productoId, cantidad, ubicacion);
    }

    @MutationMapping
    public InventarioDTO actualizarCantidadInventario(@Argument Long productoId, @Argument Integer cantidad) {
        return inventarioService.actualizarCantidad(productoId, cantidad);
    }
}
