package com.hotel.paraiso.repository;

import com.hotel.paraiso.model.Servicio;
import com.hotel.paraiso.model.Servicio.CategoriaServicio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ServicioRepository extends JpaRepository<Servicio, Long> {

    Optional<Servicio> findByNombreIgnoreCase(String nombre);

    boolean existsByNombreIgnoreCase(String nombre);

    List<Servicio> findByActivoTrue();

    List<Servicio> findByCategoria(CategoriaServicio categoria);
}
