package com.gllanos.securedocs.abac;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AbacResult {
    private boolean permitido;
    private String politicaFallida;
    private String motivo;
}