package com.gllanos.securedocs.rbac;

import com.gllanos.securedocs.model.Permiso;
import com.gllanos.securedocs.model.Rol;
import com.gllanos.securedocs.model.Usuario;
import com.gllanos.securedocs.repository.PermisoRepository;
import com.gllanos.securedocs.repository.RolPermisoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RbacService {

    private final RolPermisoRepository rolPermisoRepository;
    private final PermisoRepository permisoRepository;

    public boolean tienePermiso(Usuario usuario, Permiso.PermisoNombre permisoNombre) {
        Rol rol = usuario.getRol();

        Permiso permiso = permisoRepository.findByNombre(permisoNombre)
                .orElse(null);

        if (permiso == null) return false;

        return rolPermisoRepository.existsByRolAndPermiso(rol, permiso);
    }
}