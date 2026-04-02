package com.ecommerce.backend.model;


import com.ecommerce.backend.model.Enum.EstadoPago;
import com.ecommerce.backend.model.Enum.MedioPago;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "pasarela_pago")
public class PasarelaPago {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "id_orden", nullable = false, unique = true)
    private Orden orden;

    @Column(name = "monto", nullable = false, precision = 10, scale = 2)
    private BigDecimal monto;

    @Enumerated(EnumType.STRING)
    @Column(name = "medio_pago", nullable = false, length = 20)
    private MedioPago medioPago;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado", nullable = false, length = 20)
    private EstadoPago estado = EstadoPago.PENDIENTE;

    @Column(name = "codigo_transaccion", length = 100)
    private String codigoTransaccion;

    @Column(name = "fecha", nullable = false)
    private LocalDateTime fecha;

    @CreationTimestamp
    @Column(name = "creacion", nullable = false, updatable = false)
    private LocalDateTime creacion;

    @UpdateTimestamp
    @Column(name = "actualizacion", nullable = false)
    private LocalDateTime actualizacion;
}
