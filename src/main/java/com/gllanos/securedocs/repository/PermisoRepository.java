package com.gllanos.securedocs.repository;

import com.gllanos.securedocs.model.Permiso;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PermisoRepository extends JpaRepository<Permiso, Long> {
    Optional<Permiso> findByNombre(Permiso.PermisoNombre nombre);
}