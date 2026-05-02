package com.ecommerce.backend.controller.rest;

import com.ecommerce.backend.dto.ApiResponse;
import com.ecommerce.backend.dto.ComPasarelaPagoDTO.CrearPagoDTO;
import com.ecommerce.backend.dto.ComPasarelaPagoDTO.PasarelaPagoDTO;
import com.ecommerce.backend.model.Enum.EstadoPago;
import com.ecommerce.backend.service.PasarelaPagoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PasarelaPagoControllerTest {

    @Mock
    private PasarelaPagoService pasarelaPagoService;

    @InjectMocks
    private PasarelaPagoController pasarelaPagoController;

    private PasarelaPagoDTO pasarelaPagoDTO;
    private CrearPagoDTO crearPagoDTO;
    private List<PasarelaPagoDTO> pasarelaPagoDTOList;

    @BeforeEach
    void setUp() {
        pasarelaPagoDTO = PasarelaPagoDTO.builder()
                .id(1L)
                .ordenId(10L)
                .monto(BigDecimal.valueOf(150.00))
                .estado(EstadoPago.PENDIENTE)
                .fecha(LocalDateTime.now())
                .build();

        crearPagoDTO = CrearPagoDTO.builder()
                .ordenId(10L)
                .monto(BigDecimal.valueOf(150.00))
                .build();

        pasarelaPagoDTOList = Arrays.asList(
                pasarelaPagoDTO,
                PasarelaPagoDTO.builder().id(2L).ordenId(11L).monto(BigDecimal.valueOf(200.00)).estado(EstadoPago.APROBADO).build()
        );
    }

    @Test
    @DisplayName("Debe procesar un nuevo pago y retornar 201 CREATED")
    void debeProcesarPago() {
        when(pasarelaPagoService.procesarPago(any(CrearPagoDTO.class))).thenReturn(pasarelaPagoDTO);

        ResponseEntity<ApiResponse<PasarelaPagoDTO>> response = pasarelaPagoController.procesarPago(crearPagoDTO);

        assertNotNull(response);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(201, response.getBody().getStatus());
        assertEquals(pasarelaPagoDTO, response.getBody().getData());
        verify(pasarelaPagoService, times(1)).procesarPago(any(CrearPagoDTO.class));
    }

    @Test
    @DisplayName("Debe buscar pago por ID y retornar 200 OK")
    void debeBuscarPorId() {
        when(pasarelaPagoService.buscarPorId(anyLong())).thenReturn(pasarelaPagoDTO);

        ResponseEntity<ApiResponse<PasarelaPagoDTO>> response = pasarelaPagoController.buscarPorId(1L);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(200, response.getBody().getStatus());
        assertEquals(pasarelaPagoDTO, response.getBody().getData());
        verify(pasarelaPagoService, times(1)).buscarPorId(1L);
    }

    @Test
    @DisplayName("Debe buscar pago por ID de orden y retornar 200 OK")
    void debeBuscarPorOrden() {
        when(pasarelaPagoService.buscarPorOrden(anyLong())).thenReturn(pasarelaPagoDTO);

        ResponseEntity<ApiResponse<PasarelaPagoDTO>> response = pasarelaPagoController.buscarPorOrden(10L);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(200, response.getBody().getStatus());
        assertEquals(pasarelaPagoDTO, response.getBody().getData());
        verify(pasarelaPagoService, times(1)).buscarPorOrden(10L);
    }

    @Test
    @DisplayName("Debe listar pagos por estado y retornar 200 OK")
    void debeListarPorEstado() {
        when(pasarelaPagoService.listarPorEstado(any(EstadoPago.class))).thenReturn(pasarelaPagoDTOList);

        ResponseEntity<ApiResponse<List<PasarelaPagoDTO>>> response = pasarelaPagoController.listarPorEstado(EstadoPago.PENDIENTE);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(200, response.getBody().getStatus());
        assertEquals(pasarelaPagoDTOList.size(), response.getBody().getData().size());
        verify(pasarelaPagoService, times(1)).listarPorEstado(EstadoPago.PENDIENTE);
    }

    @Test
    @DisplayName("Debe confirmar un pago y retornar 200 OK")
    void debeConfirmarPago() {
        when(pasarelaPagoService.confirmarPago(anyLong())).thenReturn(pasarelaPagoDTO);

        ResponseEntity<ApiResponse<PasarelaPagoDTO>> response = pasarelaPagoController.confirmar(1L);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(200, response.getBody().getStatus());
        assertEquals(pasarelaPagoDTO, response.getBody().getData());
        verify(pasarelaPagoService, times(1)).confirmarPago(1L);
    }

    @Test
    @DisplayName("Debe rechazar un pago y retornar 200 OK")
    void debeRechazarPago() {
        when(pasarelaPagoService.rechazarPago(anyLong())).thenReturn(pasarelaPagoDTO);

        ResponseEntity<ApiResponse<PasarelaPagoDTO>> response = pasarelaPagoController.rechazar(1L);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(200, response.getBody().getStatus());
        assertEquals(pasarelaPagoDTO, response.getBody().getData());
        verify(pasarelaPagoService, times(1)).rechazarPago(1L);
    }
}
