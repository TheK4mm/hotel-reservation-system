package com.hotel.paraiso.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Entidad que representa un empleado del hotel
 * (recepcionista, gerente, etc.) responsable de las reservas.
 */
@Entity
@Table(name = "empleados", indexes = {
        @Index(name = "idx_empleado_documento", columnList = "numero_documento", unique = true)
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(exclude = "reservas")
public class Empleado {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nombre", nullable = false, length = 100)
    private String nombre;

    @Column(name = "apellido", nullable = false, length = 100)
    private String apellido;

    @Column(name = "numero_documento", nullable = false, unique = true, length = 30)
    private String numeroDocumento;

    @Column(name = "cargo", nullable = false, length = 80)
    private String cargo; // ej: "Recepcionista", "Gerente", "Conserje"

    @Column(name = "email_corporativo", length = 150)
    private String emailCorporativo;

    @Column(name = "telefono_extension", length = 10)
    private String telefonoExtension;

    @Column(name = "fecha_contratacion", nullable = false)
    private LocalDate fechaContratacion;

    @Column(name = "activo", nullable = false)
    @Builder.Default
    private Boolean activo = true;

    @CreationTimestamp
    @Column(name = "fecha_registro", updatable = false)
    private LocalDateTime fechaRegistro;

    // ─── Relaciones ────────────────────────────────────
    // Un Empleado puede gestionar muchas Reservas (1:N)
    @OneToMany(mappedBy = "empleado", cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    private List<Reserva> reservas;
}
