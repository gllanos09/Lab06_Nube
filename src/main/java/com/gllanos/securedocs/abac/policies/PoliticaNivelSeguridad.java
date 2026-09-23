package com.gllanos.securedocs.abac.policies;

import com.gllanos.securedocs.abac.AbacPolicy;
import com.gllanos.securedocs.abac.EntornoRequest;
import com.gllanos.securedocs.model.Documento;
import com.gllanos.securedocs.model.Usuario;
import org.springframework.stereotype.Component;

@Component
public class PoliticaNivelSeguridad implements AbacPolicy {

    @Override
    public String getNombre() { return "NIVEL_SEGURIDAD"; }

    @Override
    public boolean evaluar(Usuario usuario, Documento documento,
                           String accion, EntornoRequest entorno) {
        return usuario.getNivelSeguridad() >= documento.getNivelConfidencialidad();
    }
}