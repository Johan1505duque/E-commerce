package com.ecommerce.backend.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ApiResponse<T> {
    private int status;
    private String mensaje;
    private T data;

    public static <T> ApiResponse<T> exito(T data, String mensaje) {
        return ApiResponse.<T>builder()
                .status(200)
                .mensaje(mensaje)
                .data(data)
                .build();
    }

    public static <T> ApiResponse<T> creado(T data, String mensaje) {
        return ApiResponse.<T>builder()
                .status(201)
                .mensaje(mensaje)
                .data(data)
                .build();
    }

    public static <T> ApiResponse<T> error(int status, String mensaje) {
        return ApiResponse.<T>builder()
                .status(status)
                .mensaje(mensaje)
                .data(null)
                .build();
    }
}
