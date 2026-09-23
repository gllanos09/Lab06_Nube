package com.gllanos.securedocs.abac.policies;

import com.gllanos.securedocs.abac.AbacPolicy;
import com.gllanos.securedocs.abac.EntornoRequest;
import com.gllanos.securedocs.model.Documento;
import com.gllanos.securedocs.model.Rol;
import com.gllanos.securedocs.model.Usuario;
import org.springframework.stereotype.Component;

@Component
public class PoliticaInvitado implements AbacPolicy {

    @Override
    public String getNombre() { return "INVITADO"; }

    @Override
    public boolean evaluar(Usuario usuario, Documento documento,
                           String accion, EntornoRequest entorno) {
        if (usuario.getRol().getNombre() != Rol.RolNombre.INVITADO) return true;

        return usuario.getTipoContrato() == Usuario.TipoContrato.EXTERNO
                && documento.getNivelConfidencialidad() <= 1
                && documento.getEstado() == Documento.EstadoDocumento.PUBLICADO;
    }
}