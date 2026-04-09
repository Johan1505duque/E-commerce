package com.ecommerce.backend.dto.IA;


import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Respuesta generada por la IA")
public class AiResponseDTO {
    @Schema(description = "Texto generado por la IA", example = "Una API es...")
    private String respuesta;

    public AiResponseDTO(String respuesta) {
        this.respuesta = respuesta;
    }

    public String getRespuesta() {
        return respuesta;
    }
}
