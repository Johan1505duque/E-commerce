package com.ecommerce.backend.service;

import com.ecommerce.backend.model.Envio;
import com.ecommerce.backend.model.Orden;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j // Para usar el logger de Lombok
public class NotificacionService {

    public void notificarCambioEstadoEnvio(Envio envio) {
        log.info("📢 Notificación enviada al cliente para el envío ID: {} (Orden ID: {})",
                envio.getId(), envio.getOrden().getId());
        log.info("   Estado actual: {}", envio.getEstado());
        log.info("   Código de rastreo: {}", envio.getCodigoRastreo() != null ? envio.getCodigoRastreo() : "N/A");
        log.info("   Mensaje: Su pedido con orden ID {} ha cambiado a estado '{}'.",
                envio.getOrden().getId(), envio.getEstado().name());
        // Aquí iría la lógica real para enviar email/SMS/Push
    }

    public void notificarCreacionEnvio(Envio envio) {
        log.info("📢 Notificación enviada al cliente: Envío creado para la Orden ID: {}", envio.getOrden().getId());
        log.info("   Dirección de envío: {}", envio.getDireccionEnvio());
        log.info("   Mensaje: Su pedido con orden ID {} ha sido preparado para envío.", envio.getOrden().getId());
    }

    public void notificarCreacionOrden(Orden orden) {
        log.info("📢 Notificación enviada al cliente: Orden ID {} creada exitosamente.", orden.getId());
        log.info("   Total: {}", orden.getTotal());
        log.info("   Estado: {}", orden.getEstado());
    }
}
