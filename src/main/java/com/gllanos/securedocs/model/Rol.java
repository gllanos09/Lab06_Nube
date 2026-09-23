package com.gllanos.securedocs.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "roles")
public class Rol {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    @Enumerated(EnumType.STRING)
    private RolNombre nombre;

    public enum RolNombre {
        ADMINISTRADOR, GERENTE, SUPERVISOR, EMPLEADO, AUDITOR, INVITADO
    }
}