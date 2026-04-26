package com.hotel.paraiso.repository;

import com.hotel.paraiso.model.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Long> {

    Optional<Cliente> findByEmail(String email);

    Optional<Cliente> findByNumeroDocumento(String numeroDocumento);

    boolean existsByEmail(String email);

    boolean existsByNumeroDocumento(String numeroDocumento);

    List<Cliente> findByActivoTrue();

    List<Cliente> findByNombreContainingIgnoreCaseOrApellidoContainingIgnoreCase(
            String nombre, String apellido);
}
