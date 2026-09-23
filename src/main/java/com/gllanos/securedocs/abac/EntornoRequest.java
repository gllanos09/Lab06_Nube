package com.gllanos.securedocs.abac;

import lombok.Data;

@Data
public class EntornoRequest {
    private String hora;           // formato "HH:mm"
    private String direccionIp;
    private String ubicacion;      // "PERU", "USA", etc.
    private String dispositivo;    // "CORPORATIVO" o "PERSONAL"
}