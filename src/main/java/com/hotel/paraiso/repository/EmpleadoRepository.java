package com.hotel.paraiso.repository;

import com.hotel.paraiso.model.Empleado;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EmpleadoRepository extends JpaRepository<Empleado, Long> {

    Optional<Empleado> findByNumeroDocumento(String numeroDocumento);

    boolean existsByNumeroDocumento(String numeroDocumento);

    List<Empleado> findByActivoTrue();

    List<Empleado> findByCargoIgnoreCase(String cargo);
}
