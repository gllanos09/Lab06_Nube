package com.gllanos.securedocs.abac.policies;

import com.gllanos.securedocs.abac.AbacPolicy;
import com.gllanos.securedocs.abac.EntornoRequest;
import com.gllanos.securedocs.model.Documento;
import com.gllanos.securedocs.model.Usuario;
import org.springframework.stereotype.Component;

@Component
public class PoliticaDispositivo implements AbacPolicy {

    @Override
    public String getNombre() { return "DISPOSITIVO"; }

    @Override
    public boolean evaluar(Usuario usuario, Documento documento,
                           String accion, EntornoRequest entorno) {
        if (documento.getNivelConfidencialidad() < 4) return true;

        return "CORPORATIVO".equalsIgnoreCase(entorno.getDispositivo());
    }
}