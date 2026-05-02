package com.ecommerce.backend.service;

import com.ecommerce.backend.dto.IA.AiMessage; // Importar AiMessage
import com.ecommerce.backend.dto.IA.AiRequestDTO;

public interface AiService {
    String preguntar(AiRequestDTO request);
    AiMessage getAiResponse(String message); // Nuevo método para el controlador
}
