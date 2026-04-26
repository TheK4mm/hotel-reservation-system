package com.hotel.paraiso.repository;

import com.hotel.paraiso.model.Reserva;
import com.hotel.paraiso.model.Reserva.EstadoReserva;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface ReservaRepository extends JpaRepository<Reserva, Long> {

    Optional<Reserva> findByCodigoReserva(String codigoReserva);

    boolean existsByCodigoReserva(String codigoReserva);

    List<Reserva> findByClienteId(Long clienteId);

    List<Reserva> findByEmpleadoId(Long empleadoId);

    List<Reserva> findByEstado(EstadoReserva estado);

    List<Reserva> findByFechaEntradaBetween(LocalDate desde, LocalDate hasta);

    /** Cuenta reservas activas de una habitación en un rango de fechas */
    @Query("""
            SELECT COUNT(r) FROM Reserva r
            JOIN r.habitaciones h
            WHERE h.id = :habitacionId
              AND r.estado NOT IN ('CANCELADA', 'NO_SHOW', 'CHECKOUT')
              AND r.fechaEntrada < :fechaSalida
              AND r.fechaSalida > :fechaEntrada
            """)
    long countReservasActivasParaHabitacion(
            @Param("habitacionId") Long habitacionId,
            @Param("fechaEntrada") LocalDate fechaEntrada,
            @Param("fechaSalida") LocalDate fechaSalida
    );

    /** Obtiene el último código de reserva para generar el siguiente */
    @Query("SELECT r.codigoReserva FROM Reserva r ORDER BY r.id DESC LIMIT 1")
    Optional<String> findLastCodigoReserva();
}
