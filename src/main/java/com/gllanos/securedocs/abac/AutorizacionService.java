package com.gllanos.securedocs.abac;

import com.gllanos.securedocs.model.Documento;
import com.gllanos.securedocs.model.Permiso;
import com.gllanos.securedocs.model.Usuario;
import com.gllanos.securedocs.rbac.RbacService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AutorizacionService {

    private final RbacService rbacService;
    private final AbacService abacService;

    public AbacResult autorizar(Usuario usuario, Documento documento,
                                Permiso.PermisoNombre permiso,
                                EntornoRequest entorno) {

        // Paso 1 — RBAC
        if (!rbacService.tienePermiso(usuario, permiso)) {
            return new AbacResult(false, "RBAC",
                    "Rol " + usuario.getRol().getNombre() +
                            " no tiene permiso: " + permiso.name());
        }

        // Paso 2 — ABAC
        return abacService.evaluar(usuario, documento, permiso.name(), entorno);
    }
}