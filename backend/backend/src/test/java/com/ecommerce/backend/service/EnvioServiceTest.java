package com.ecommerce.backend.service;

import com.ecommerce.backend.dto.CrearEnvioDTO;
import com.ecommerce.backend.dto.EnvioDTO;
import com.ecommerce.backend.exception.BadRequestException;
import com.ecommerce.backend.exception.ResourceNotFoundException;
import com.ecommerce.backend.mapper.EnvioMapper;
import com.ecommerce.backend.model.Envio;
import com.ecommerce.backend.model.Enum.EstadoEnvio;
import com.ecommerce.backend.model.Orden;
import com.ecommerce.backend.repository.EnvioRepository;
import com.ecommerce.backend.repository.OrdenRepository;
import com.ecommerce.backend.service.impl.EnvioServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EnvioServiceTest {

    @Mock
    private EnvioRepository envioRepository;
    @Mock
    private OrdenRepository ordenRepository;
    @Mock
    private EnvioMapper envioMapper;
    @Mock
    private NotificacionService notificacionService;

    @InjectMocks
    private EnvioServiceImpl envioService;

    private Orden orden;
    private Envio envio; // Este objeto 'envio' tiene un codigoRastreo por defecto
    private EnvioDTO envioDTO;
    private CrearEnvioDTO crearEnvioDTO;

    @BeforeEach
    void setUp() {
        orden = Orden.builder().id(10L).build(); // Solo necesitamos el ID de la orden

        envio = Envio.builder()
                .id(1L)
                .orden(orden)
                .direccionEnvio("Calle Falsa 123")
                .estado(EstadoEnvio.PENDIENTE)
                .codigoRastreo("TRACK123") // Este es el código de rastreo inicial
                .creacion(LocalDateTime.now())
                .actualizacion(LocalDateTime.now())
                .build();

        envioDTO = EnvioDTO.builder()
                .id(1L)
                .ordenId(10L)
                .direccionEnvio("Calle Falsa 123")
                .estado(EstadoEnvio.PENDIENTE)
                .codigoRastreo("TRACK123")
                .creacion(LocalDateTime.now())
                .actualizacion(LocalDateTime.now())
                .build();

        crearEnvioDTO = CrearEnvioDTO.builder()
                .ordenId(10L)
                .direccionEnvio("Calle Falsa 123")
                .build();
    }

    @Test
    @DisplayName("Debe crear un envío correctamente")
    void debeCrearEnvio() {
        when(ordenRepository.findById(anyLong())).thenReturn(Optional.of(orden));
        when(envioRepository.existsByOrdenId(anyLong())).thenReturn(false);
        when(envioMapper.toEntity(any(CrearEnvioDTO.class))).thenReturn(envio);
        when(envioRepository.save(any(Envio.class))).thenReturn(envio);
        when(envioMapper.toDTO(any(Envio.class))).thenReturn(envioDTO);

        EnvioDTO result = envioService.crearEnvio(crearEnvioDTO);

        assertNotNull(result);
        assertEquals(envioDTO.getOrdenId(), result.getOrdenId());
        assertEquals(EstadoEnvio.PENDIENTE, result.getEstado());
        assertNotNull(result.getCodigoRastreo());
        verify(notificacionService, times(1)).notificarCreacionEnvio(any(Envio.class));
        verify(envioRepository, times(1)).save(any(Envio.class));
    }

    @Test
    @DisplayName("Debe lanzar ResourceNotFoundException si la orden no existe al crear envío")
    void debeLanzarExcepcionSiOrdenNoExisteAlCrearEnvio() {
        when(ordenRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> envioService.crearEnvio(crearEnvioDTO));
        verify(envioRepository, never()).save(any(Envio.class));
    }

    @Test
    @DisplayName("Debe lanzar BadRequestException si ya existe un envío para la orden")
    void debeLanzarExcepcionSiEnvioYaExisteParaOrden() {
        when(ordenRepository.findById(anyLong())).thenReturn(Optional.of(orden));
        when(envioRepository.existsByOrdenId(anyLong())).thenReturn(true);

        assertThrows(BadRequestException.class, () -> envioService.crearEnvio(crearEnvioDTO));
        verify(envioRepository, never()).save(any(Envio.class));
    }

    @Test
    @DisplayName("Debe obtener un envío por ID")
    void debeObtenerEnvioPorId() {
        when(envioRepository.findById(anyLong())).thenReturn(Optional.of(envio));
        when(envioMapper.toDTO(any(Envio.class))).thenReturn(envioDTO);

        EnvioDTO result = envioService.obtenerEnvioPorId(1L);

        assertNotNull(result);
        assertEquals(envioDTO.getId(), result.getId());
        verify(envioRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("Debe lanzar ResourceNotFoundException si el envío por ID no existe")
    void debeLanzarExcepcionSiEnvioPorIdNoExiste() {
        when(envioRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> envioService.obtenerEnvioPorId(99L));
    }

    @Test
    @DisplayName("Debe obtener un envío por ID de orden")
    void debeObtenerEnvioPorOrdenId() {
        when(envioRepository.findByOrdenId(anyLong())).thenReturn(Optional.of(envio));
        when(envioMapper.toDTO(any(Envio.class))).thenReturn(envioDTO);

        EnvioDTO result = envioService.obtenerEnvioPorOrdenId(10L);

        assertNotNull(result);
        assertEquals(envioDTO.getOrdenId(), result.getOrdenId());
        verify(envioRepository, times(1)).findByOrdenId(10L);
    }

    @Test
    @DisplayName("Debe lanzar ResourceNotFoundException si el envío por ID de orden no existe")
    void debeLanzarExcepcionSiEnvioPorOrdenIdNoExiste() {
        when(envioRepository.findByOrdenId(anyLong())).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> envioService.obtenerEnvioPorOrdenId(99L));
    }

    @Test
    @DisplayName("Debe actualizar el estado del envío")
    void debeActualizarEstadoEnvio() {
        Envio updatedEnvio = Envio.builder().id(1L).orden(orden).estado(EstadoEnvio.EN_CAMINO).build();
        EnvioDTO updatedEnvioDTO = EnvioDTO.builder().id(1L).ordenId(10L).estado(EstadoEnvio.EN_CAMINO).build();

        when(envioRepository.findById(anyLong())).thenReturn(Optional.of(envio));
        when(envioRepository.save(any(Envio.class))).thenReturn(updatedEnvio);
        when(envioMapper.toDTO(any(Envio.class))).thenReturn(updatedEnvioDTO);

        EnvioDTO result = envioService.actualizarEstadoEnvio(1L, EstadoEnvio.EN_CAMINO);

        assertNotNull(result);
        assertEquals(EstadoEnvio.EN_CAMINO, result.getEstado());
        verify(notificacionService, times(1)).notificarCambioEstadoEnvio(any(Envio.class));
        verify(envioRepository, times(1)).save(any(Envio.class));
    }

    @Test
    @DisplayName("Debe lanzar BadRequestException si el estado es el mismo al actualizar")
    void debeLanzarExcepcionSiEstadoEsElMismo() {
        when(envioRepository.findById(anyLong())).thenReturn(Optional.of(envio)); // Estado inicial PENDIENTE

        assertThrows(BadRequestException.class, () -> envioService.actualizarEstadoEnvio(1L, EstadoEnvio.PENDIENTE));
        verify(envioRepository, never()).save(any(Envio.class));
    }

    @Test
    @DisplayName("Debe asignar código de rastreo")
    void debeAsignarCodigoRastreo() {
        // Creamos un objeto Envio que NO tiene código de rastreo inicialmente
        Envio envioSinCodigo = Envio.builder()
                .id(1L)
                .orden(orden)
                .direccionEnvio("Calle Falsa 123")
                .estado(EstadoEnvio.PENDIENTE)
                .codigoRastreo(null) // Importante: null para que el servicio pueda asignarlo
                .creacion(LocalDateTime.now())
                .actualizacion(LocalDateTime.now())
                .build();

        Envio updatedEnvio = Envio.builder().id(1L).orden(orden).codigoRastreo("NEWTRACK456").build();
        EnvioDTO updatedEnvioDTO = EnvioDTO.builder().id(1L).ordenId(10L).codigoRastreo("NEWTRACK456").build();

        // Mockeamos para que findById devuelva el envío sin código
        when(envioRepository.findById(anyLong())).thenReturn(Optional.of(envioSinCodigo));
        when(envioRepository.save(any(Envio.class))).thenReturn(updatedEnvio);
        when(envioMapper.toDTO(any(Envio.class))).thenReturn(updatedEnvioDTO);

        EnvioDTO result = envioService.asignarCodigoRastreo(1L, "NEWTRACK456");

        assertNotNull(result);
        assertEquals("NEWTRACK456", result.getCodigoRastreo());
        verify(envioRepository, times(1)).save(any(Envio.class));
    }

    @Test
    @DisplayName("Debe lanzar BadRequestException si el código de rastreo ya está asignado")
    void debeLanzarExcepcionSiCodigoRastreoYaAsignado() {
        envio.setCodigoRastreo("EXISTING_CODE"); // Asignar un código existente para este test
        when(envioRepository.findById(anyLong())).thenReturn(Optional.of(envio));

        assertThrows(BadRequestException.class, () -> envioService.asignarCodigoRastreo(1L, "NEW_CODE"));
        verify(envioRepository, never()).save(any(Envio.class));
    }

    @Test
    @DisplayName("Debe listar todos los envíos")
    void debeListarTodosLosEnvios() {
        List<Envio> envios = Arrays.asList(envio, Envio.builder().id(2L).orden(orden).build());
        List<EnvioDTO> enviosDTO = Arrays.asList(envioDTO, EnvioDTO.builder().id(2L).ordenId(10L).build());

        when(envioRepository.findAll()).thenReturn(envios);
        when(envioMapper.toDTOList(anyList())).thenReturn(enviosDTO);

        List<EnvioDTO> result = envioService.listarTodosLosEnvios();

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(envioRepository, times(1)).findAll();
    }
}
