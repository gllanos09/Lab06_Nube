package com.gllanos.securedocs.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "usuarios")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nombre;

    @Column(nullable = false, unique = true)
    private String correo;

    @Column(nullable = false)
    private String password;

    @ManyToOne
    @JoinColumn(name = "rol_id", nullable = false)
    private Rol rol;

    @ManyToOne
    @JoinColumn(name = "departamento_id")
    private Departamento departamento;

    @Column(nullable = false)
    private Integer nivelSeguridad;

    @Column(nullable = false)
    private String pais;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private TipoContrato tipoContrato;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private EstadoUsuario estado;

    public enum TipoContrato {
        INTERNO, EXTERNO
    }

    public enum EstadoUsuario {
        ACTIVO, INACTIVO, SUSPENDIDO
    }
}