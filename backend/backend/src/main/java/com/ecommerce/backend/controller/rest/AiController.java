package com.ecommerce.backend.controller.rest;

import com.ecommerce.backend.dto.ApiResponse;
import com.ecommerce.backend.dto.IA.AiMessage;
import com.ecommerce.backend.service.AiService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/ai")
@RequiredArgsConstructor
@Tag(name = "Inteligencia Artificial", description = "Endpoints para interactuar con IA")
public class AiController {

    private final AiService aiService;

    @GetMapping("/chat")
    @Operation(summary = "Obtener respuesta de la IA")
    public ResponseEntity<ApiResponse<AiMessage>> chat(@RequestParam String message) {
        AiMessage response = aiService.getAiResponse(message);
        return ResponseEntity.ok(ApiResponse.exito(response, "Respuesta de IA obtenida"));
    }
}
