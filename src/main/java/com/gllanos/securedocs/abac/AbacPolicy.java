package com.gllanos.securedocs.abac;

import com.gllanos.securedocs.model.Documento;
import com.gllanos.securedocs.model.Usuario;

public interface AbacPolicy {
    String getNombre();
    boolean evaluar(Usuario usuario, Documento documento,
                    String accion, EntornoRequest entorno);
}