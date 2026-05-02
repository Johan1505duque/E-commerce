package com.ecommerce.backend.controller.rest;

import com.ecommerce.backend.dto.ApiResponse;
import com.ecommerce.backend.dto.IA.AiMessage;
import com.ecommerce.backend.service.AiService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AiControllerTest {

    @Mock
    private AiService aiService;

    @InjectMocks
    private AiController aiController;

    private String userMessage;
    private AiMessage aiResponse;

    @BeforeEach
    void setUp() {
        userMessage = "Hola, ¿qué productos me recomiendas?";
        aiResponse = new AiMessage("¡Claro! Te recomiendo nuestros últimos modelos de laptops.");
    }

    @Test
    @DisplayName("Debe obtener una respuesta de la IA y retornar 200 OK")
    void debeObtenerRespuestaIA() {
        when(aiService.getAiResponse(anyString())).thenReturn(aiResponse);

        ResponseEntity<ApiResponse<AiMessage>> response = aiController.chat(userMessage);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(200, response.getBody().getStatus());
        assertEquals(aiResponse, response.getBody().getData());
        verify(aiService, times(1)).getAiResponse(userMessage);
    }
}
