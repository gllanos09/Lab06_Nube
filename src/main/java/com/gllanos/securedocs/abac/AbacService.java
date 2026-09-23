package com.gllanos.securedocs.abac;

import com.gllanos.securedocs.model.Documento;
import com.gllanos.securedocs.model.Usuario;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AbacService {

    private final List<AbacPolicy> policies;

    public AbacResult evaluar(Usuario usuario, Documento documento,
                              String accion, EntornoRequest entorno) {
        for (AbacPolicy policy : policies) {
            if (!policy.evaluar(usuario, documento, accion, entorno)) {
                return new AbacResult(
                        false,
                        policy.getNombre(),
                        "Acceso denegado por política: " + policy.getNombre()
                );
            }
        }
        return new AbacResult(true, null, "Acceso autorizado");
    }
}