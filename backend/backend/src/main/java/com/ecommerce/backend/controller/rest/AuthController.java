package com.ecommerce.backend.controller.rest;

import com.ecommerce.backend.dto.ApiResponse;
import com.ecommerce.backend.dto.AuthRequest;
import com.ecommerce.backend.dto.AuthResponse;
import com.ecommerce.backend.dto.UsuarioDTO;
import com.ecommerce.backend.model.Enum.Rol;
import com.ecommerce.backend.service.UsuarioService;
import com.ecommerce.backend.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final UsuarioService usuarioService; // Para el registro de usuarios

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<AuthResponse>> login(@RequestBody @Valid AuthRequest authRequest) {
        // 1. Autenticar al usuario con Spring Security
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(authRequest.getEmail(), authRequest.getPassword())
        );

        // 2. Si la autenticación es exitosa, generar el token JWT
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String jwt = jwtUtil.generateToken(userDetails);

        // 3. Devolver el token en la respuesta
        return ResponseEntity.ok(ApiResponse.exito(new AuthResponse(jwt), "Login exitoso"));
    }

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<UsuarioDTO>> register(@RequestBody @Valid UsuarioDTO usuarioDTO) {
        // Asignar un rol por defecto si no viene (ej. CUSTOMER)
        if (usuarioDTO.getRol() == null) {
            usuarioDTO.setRol(Rol.CLIENTE);
        }
        // La contraseña debe ser codificada en el servicio
        UsuarioDTO nuevoUsuario = usuarioService.guardar(usuarioDTO);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.creado(nuevoUsuario, "Usuario registrado exitosamente"));
    }
}
