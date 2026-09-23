package com.gllanos.securedocs.abac.policies;

import com.gllanos.securedocs.abac.AbacPolicy;
import com.gllanos.securedocs.abac.EntornoRequest;
import com.gllanos.securedocs.model.Documento;
import com.gllanos.securedocs.model.Rol;
import com.gllanos.securedocs.model.Usuario;
import org.springframework.stereotype.Component;

@Component
public class PoliticaPropiedad implements AbacPolicy {

    @Override
    public String getNombre() { return "PROPIEDAD"; }

    @Override
    public boolean evaluar(Usuario usuario, Documento documento,
                           String accion, EntornoRequest entorno) {
        if (!accion.equals("MODIFICAR_DOCUMENTO")) return true;

        Rol.RolNombre rol = usuario.getRol().getNombre();
        if (rol == Rol.RolNombre.ADMINISTRADOR || rol == Rol.RolNombre.GERENTE)
            return true;

        return usuario.getId().equals(documento.getPropietario().getId());
    }
}