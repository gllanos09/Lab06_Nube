package com.gllanos.securedocs.repository;

import com.gllanos.securedocs.model.Rol;
import com.gllanos.securedocs.model.RolPermiso;
import com.gllanos.securedocs.model.Permiso;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RolPermisoRepository extends JpaRepository<RolPermiso, Long> {
    List<RolPermiso> findByRol(Rol rol);
    boolean existsByRolAndPermiso(Rol rol, Permiso permiso);
}