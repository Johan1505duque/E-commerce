package com.ecommerce.backend.service.impl;

import com.ecommerce.backend.service.PasarelaPagoService;
import com.ecommerce.backend.repository.PasarelaPagoRepository;
import com.ecommerce.backend.repository.OrdenRepository;
import com.ecommerce.backend.mapper.PasarelaPagoMapper;
import com.ecommerce.backend.model.*;
import com.ecommerce.backend.dto.ComOrdenDTO.*;
import com.ecommerce.backend.dto.ComPasarelaPagoDTO.*;
import com.ecommerce.backend.exception.*;
import com.ecommerce.backend.model.Enum.*;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PasarelaPagoServiceImpl implements PasarelaPagoService {

    private final PasarelaPagoRepository pasarelaPagoRepository;
    private final OrdenRepository ordenRepository;
    private final PasarelaPagoMapper pasarelaPagoMapper;

    @Override
    @Transactional
    public PasarelaPagoDTO procesarPago(CrearPagoDTO dto) {

        // 1. Verificar que la orden existe
        Orden orden = ordenRepository.findById(dto.getOrdenId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Orden no encontrada con id: " + dto.getOrdenId()));

        // 2. Verificar que la orden no está cancelada
        if (orden.getEstado() == EstadoOrden.CANCELADO) {
            throw new BadRequestException(
                    "No se puede pagar una orden cancelada");
        }

        // 3. Verificar que no tenga pago ya registrado
        if (pasarelaPagoRepository.existsByOrdenId(dto.getOrdenId())) {
            throw new BadRequestException(
                    "Esta orden ya tiene un pago registrado");
        }

        // 4. Generar código de transacción único
        String codigoTransaccion = "TXN-" + System.currentTimeMillis();

        // 5. Crear el pago
        PasarelaPago pago = PasarelaPago.builder()
                .orden(orden)
                .monto(orden.getTotal())
                .medioPago(dto.getMedioPago())
                .estado(EstadoPago.PENDIENTE)
                .codigoTransaccion(codigoTransaccion)
                .fecha(LocalDateTime.now())
                .build();

        return pasarelaPagoMapper.toDTO(pasarelaPagoRepository.save(pago));
    }

    @Override
    public PasarelaPagoDTO buscarPorOrden(Long ordenId) {
        return pasarelaPagoMapper.toDTO(
                pasarelaPagoRepository.findByOrdenId(ordenId)
                        .orElseThrow(() -> new ResourceNotFoundException(
                                "Pago no encontrado para la orden: " + ordenId)));
    }

    @Override
    public PasarelaPagoDTO buscarPorId(Long id) {
        return pasarelaPagoMapper.toDTO(
                pasarelaPagoRepository.findById(id)
                        .orElseThrow(() -> new ResourceNotFoundException(
                                "Pago no encontrado con id: " + id)));
    }

    @Override
    public List<PasarelaPagoDTO> listarPorEstado(EstadoPago estado) {
        return pasarelaPagoMapper.toDTOList(
                pasarelaPagoRepository.findByEstado(estado));
    }

    @Override
    @Transactional
    public PasarelaPagoDTO confirmarPago(Long id) {
        PasarelaPago pago = pasarelaPagoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Pago no encontrado con id: " + id));

        // Actualizar estado del pago
        pago.setEstado(EstadoPago.APROBADO);

        // Actualizar estado de la orden
        Orden orden = pago.getOrden();
        orden.setEstado(EstadoOrden.PAGADO);
        ordenRepository.save(orden);

        return pasarelaPagoMapper.toDTO(pasarelaPagoRepository.save(pago));
    }

    @Override
    @Transactional
    public PasarelaPagoDTO rechazarPago(Long id) {
        PasarelaPago pago = pasarelaPagoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Pago no encontrado con id: " + id));

        pago.setEstado(EstadoPago.RECHAZADO);

        return pasarelaPagoMapper.toDTO(pasarelaPagoRepository.save(pago));
    }
}
