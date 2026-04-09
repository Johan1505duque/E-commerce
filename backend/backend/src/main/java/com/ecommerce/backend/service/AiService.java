package com.ecommerce.backend.service;

import com.ecommerce.backend.dto.IA.AiRequestDTO;
public interface AiService {
    String preguntar(AiRequestDTO request);
}
