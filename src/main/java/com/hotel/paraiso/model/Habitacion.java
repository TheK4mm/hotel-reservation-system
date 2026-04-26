package com.hotel.paraiso.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

/**
 * Entidad que representa una habitación física del hotel.
 */
@Entity
@Table(name = "habitaciones")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(exclude = {"tipoHabitacion", "reservas"})
public class Habitacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "numero", nullable = false, unique = true, length = 10)
    private String numero; // ej: "101", "305B"

    @Column(name = "piso", nullable = false)
    private Integer piso;

    @Column(name = "descripcion", length = 500)
    private String descripcion;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado", nullable = false)
    @Builder.Default
    private EstadoHabitacion estado = EstadoHabitacion.DISPONIBLE;

    @Column(name = "activo", nullable = false)
    @Builder.Default
    private Boolean activo = true;

    // ─── Relaciones ────────────────────────────────────
    // Muchas Habitaciones pertenecen a un TipoHabitacion (N:1)
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "tipo_habitacion_id", nullable = false)
    private TipoHabitacion tipoHabitacion;

    // Una Habitacion puede estar en muchas Reservas y viceversa (N:M)
    @ManyToMany(mappedBy = "habitaciones", fetch = FetchType.LAZY)
    private List<Reserva> reservas;

    public enum EstadoHabitacion {
        DISPONIBLE, OCUPADA, MANTENIMIENTO, BLOQUEADA
    }
}
