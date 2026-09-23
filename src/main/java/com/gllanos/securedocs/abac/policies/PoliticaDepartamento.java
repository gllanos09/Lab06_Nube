package com.gllanos.securedocs.abac.policies;

import com.gllanos.securedocs.abac.AbacPolicy;
import com.gllanos.securedocs.abac.EntornoRequest;
import com.gllanos.securedocs.model.Documento;
import com.gllanos.securedocs.model.Usuario;
import org.springframework.stereotype.Component;

@Component
public class PoliticaDepartamento implements AbacPolicy {

    @Override
    public String getNombre() { return "DEPARTAMENTO"; }

    @Override
    public boolean evaluar(Usuario usuario, Documento documento,
                           String accion, EntornoRequest entorno) {
        if (usuario.getDepartamento() == null || documento.getDepartamento() == null)
            return false;
        return usuario.getDepartamento().getId()
                .equals(documento.getDepartamento().getId());
    }
}