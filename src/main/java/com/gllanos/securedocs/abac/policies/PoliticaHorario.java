package com.gllanos.securedocs.abac.policies;

import com.gllanos.securedocs.abac.AbacPolicy;
import com.gllanos.securedocs.abac.EntornoRequest;
import com.gllanos.securedocs.model.Documento;
import com.gllanos.securedocs.model.Usuario;
import org.springframework.stereotype.Component;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

@Component
public class PoliticaHorario implements AbacPolicy {

    private static final LocalTime INICIO = LocalTime.of(8, 0);
    private static final LocalTime FIN = LocalTime.of(18, 0);

    @Override
    public String getNombre() { return "HORARIO"; }

    @Override
    public boolean evaluar(Usuario usuario, Documento documento,
                           String accion, EntornoRequest entorno) {
        if (documento.getNivelConfidencialidad() < 4) return true;

        if (entorno.getHora() == null) return false;

        LocalTime hora = LocalTime.parse(entorno.getHora(),
                DateTimeFormatter.ofPattern("HH:mm"));

        return !hora.isBefore(INICIO) && !hora.isAfter(FIN);
    }
}