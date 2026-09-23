package com.gllanos.securedocs.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "auditoria")
public class Auditoria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String usuario;

    @Column(nullable = false)
    private String recurso;

    @Column(nullable = false)
    private String accion;

    @Column(nullable = false)
    private LocalDateTime fecha;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private ResultadoAcceso resultado;

    private String motivo;

    private String direccionIp;

    private String dispositivo;

    private String ubicacion;

    @PrePersist
    public void prePersist() {
        this.fecha = LocalDateTime.now();
    }

    public enum ResultadoAcceso {
        PERMITIDO, DENEGADO
    }
}