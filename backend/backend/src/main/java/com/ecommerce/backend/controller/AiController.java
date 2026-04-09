package com.ecommerce.backend.controller;

import com.ecommerce.backend.dto.IA.AiRequestDTO;
import com.ecommerce.backend.dto.IA.AiResponseDTO;
import com.ecommerce.backend.service.AiService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/ai")
@RequiredArgsConstructor
@Tag(name = "IA", description = "Endpoints para interacción con IA")
public class AiController {

    private final AiService aiService;

    @PostMapping("/preguntar")
    public AiResponseDTO preguntar(@RequestBody AiRequestDTO request) {

        String respuesta = aiService.preguntar(request);

        return new AiResponseDTO(respuesta);
    }
}
