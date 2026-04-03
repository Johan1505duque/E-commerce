package com.ecommerce.backend.service;

import com.ecommerce.backend.dto.ComPasarelaPagoDTO.CrearPagoDTO;
import com.ecommerce.backend.dto.ComPasarelaPagoDTO.PasarelaPagoDTO;
import com.ecommerce.backend.exception.BadRequestException;
import com.ecommerce.backend.mapper.PasarelaPagoMapper;
import com.ecommerce.backend.model.Enum.EstadoOrden;
import com.ecommerce.backend.model.Enum.EstadoPago;
import com.ecommerce.backend.model.Enum.MedioPago;
import com.ecommerce.backend.model.Orden;
import com.ecommerce.backend.model.PasarelaPago;
import com.ecommerce.backend.repository.OrdenRepository;
import com.ecommerce.backend.repository.PasarelaPagoRepository;
import com.ecommerce.backend.service.impl.PasarelaPagoServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PasarelaPagoServiceTest {

    @Mock
    private PasarelaPagoRepository pasarelaPagoRepository;

    @Mock
    private OrdenRepository ordenRepository;

    @Mock
    private PasarelaPagoMapper pasarelaPagoMapper;

    @InjectMocks
    private PasarelaPagoServiceImpl pasarelaPagoService;

    private Orden orden;
    private PasarelaPago pago;
    private PasarelaPagoDTO pagoDTO;
    private CrearPagoDTO crearPagoDTO;

    @BeforeEach
    void setUp() {
        orden = Orden.builder()
                .id(1L)
                .total(new BigDecimal("90000"))
                .estado(EstadoOrden.PENDIENTE)
                .build();

        pago = PasarelaPago.builder()
                .id(1L)
                .orden(orden)
                .monto(new BigDecimal("90000"))
                .medioPago(MedioPago.PSE)
                .estado(EstadoPago.PENDIENTE)
                .build();

        pagoDTO = PasarelaPagoDTO.builder()
                .id(1L)
                .ordenId(1L)
                .monto(new BigDecimal("90000"))
                .estado(EstadoPago.PENDIENTE)
                .build();

        crearPagoDTO = new CrearPagoDTO(1L, MedioPago.PSE);
    }

    @Test
    @DisplayName("Debe procesar pago correctamente")
    void debeProcesarPago() {
        when(ordenRepository.findById(1L))
                .thenReturn(Optional.of(orden));
        when(pasarelaPagoRepository.existsByOrdenId(1L))
                .thenReturn(false);
        when(pasarelaPagoRepository.save(any()))
                .thenReturn(pago);
        when(pasarelaPagoMapper.toDTO(any()))
                .thenReturn(pagoDTO);

        PasarelaPagoDTO resultado = pasarelaPagoService
                .procesarPago(crearPagoDTO);

        assertNotNull(resultado);
        assertEquals(1L, resultado.getOrdenId());
    }

    @Test
    @DisplayName("No debe procesar pago duplicado")
    void noDebeProcesarPagoDuplicado() {
        when(ordenRepository.findById(1L))
                .thenReturn(Optional.of(orden));
        when(pasarelaPagoRepository.existsByOrdenId(1L))
                .thenReturn(true);

        assertThrows(BadRequestException.class,
                () -> pasarelaPagoService.procesarPago(crearPagoDTO));
    }

    @Test
    @DisplayName("No debe pagar orden cancelada")
    void noDebePagarOrdenCancelada() {
        orden.setEstado(EstadoOrden.CANCELADO);
        when(ordenRepository.findById(1L))
                .thenReturn(Optional.of(orden));

        assertThrows(BadRequestException.class,
                () -> pasarelaPagoService.procesarPago(crearPagoDTO));
    }

    @Test
    @DisplayName("Debe confirmar pago y actualizar orden")
    void debeConfirmarPago() {
        when(pasarelaPagoRepository.findById(1L))
                .thenReturn(Optional.of(pago));
        when(pasarelaPagoRepository.save(any()))
                .thenReturn(pago);
        when(pasarelaPagoMapper.toDTO(any()))
                .thenReturn(pagoDTO);

        PasarelaPagoDTO resultado = pasarelaPagoService
                .confirmarPago(1L);

        assertNotNull(resultado);
        verify(ordenRepository, times(1)).save(any());
    }
}
