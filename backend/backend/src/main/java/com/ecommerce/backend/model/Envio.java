package com.ecommerce.backend.model;

import com.ecommerce.backend.model.Enum.EstadoEnvio;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "envios")
public class Envio {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "id_orden", nullable = false, unique = true)
    private Orden orden;

    @Column(name = "direccion_envio", nullable = false)
    private String direccionEnvio;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado", nullable = false)
    private EstadoEnvio estado;

    @Column(name = "codigo_rastreo", unique = true)
    private String codigoRastreo;

    @CreationTimestamp
    @Column(name = "creacion", nullable = false, updatable = false)
    private LocalDateTime creacion;

    @UpdateTimestamp
    @Column(name = "actualizacion", nullable = false)
    private LocalDateTime actualizacion;
}
