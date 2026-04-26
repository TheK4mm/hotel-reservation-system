package com.hotel.paraiso.repository;

import com.hotel.paraiso.model.Pago;
import com.hotel.paraiso.model.Pago.EstadoPago;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface PagoRepository extends JpaRepository<Pago, Long> {

    List<Pago> findByReservaId(Long reservaId);

    List<Pago> findByFacturaId(Long facturaId);

    List<Pago> findByEstadoPago(EstadoPago estadoPago);

    /** Suma total de pagos aprobados para una reserva */
    @Query("""
            SELECT COALESCE(SUM(p.monto), 0) FROM Pago p
            WHERE p.reserva.id = :reservaId
              AND p.estadoPago = 'APROBADO'
            """)
    BigDecimal sumPagosAprobadosByReservaId(@Param("reservaId") Long reservaId);
}
