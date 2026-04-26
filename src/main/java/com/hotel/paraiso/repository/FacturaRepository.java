package com.hotel.paraiso.repository;

import com.hotel.paraiso.model.Factura;
import com.hotel.paraiso.model.Factura.EstadoFactura;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FacturaRepository extends JpaRepository<Factura, Long> {

    Optional<Factura> findByNumeroFactura(String numeroFactura);

    Optional<Factura> findByReservaId(Long reservaId);

    boolean existsByReservaId(Long reservaId);

    List<Factura> findByEstadoFactura(EstadoFactura estadoFactura);

    /** Obtiene el último número de factura para generar el siguiente */
    Optional<Factura> findTopByOrderByIdDesc();
}
