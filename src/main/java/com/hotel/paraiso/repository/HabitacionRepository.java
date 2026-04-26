package com.hotel.paraiso.repository;

import com.hotel.paraiso.model.Habitacion;
import com.hotel.paraiso.model.Habitacion.EstadoHabitacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface HabitacionRepository extends JpaRepository<Habitacion, Long> {

    Optional<Habitacion> findByNumero(String numero);

    boolean existsByNumero(String numero);

    List<Habitacion> findByEstado(EstadoHabitacion estado);

    List<Habitacion> findByTipoHabitacionId(Long tipoHabitacionId);

    List<Habitacion> findByActivoTrue();

    /**
     * Obtiene habitaciones disponibles para un rango de fechas.
     * Excluye habitaciones que tengan reservas confirmadas/activas
     * solapadas con el rango solicitado.
     */
    @Query("""
            SELECT h FROM Habitacion h
            WHERE h.activo = true
              AND h.estado = 'DISPONIBLE'
              AND h.id NOT IN (
                  SELECT hab.id FROM Reserva r
                  JOIN r.habitaciones hab
                  WHERE r.estado NOT IN ('CANCELADA', 'NO_SHOW', 'CHECKOUT')
                    AND r.fechaEntrada < :fechaSalida
                    AND r.fechaSalida > :fechaEntrada
              )
            """)
    List<Habitacion> findHabitacionesDisponibles(
            @Param("fechaEntrada") LocalDate fechaEntrada,
            @Param("fechaSalida") LocalDate fechaSalida
    );
}
