package com.ecommerce.backend.dto.IA;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Petición para consultar a la IA")
public class AiRequestDTO {

    @Schema(description = "Mensaje que se envía a la IA", example = "¿Qué es una API?")
    private String mensaje;

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

}
