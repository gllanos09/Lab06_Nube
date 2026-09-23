package com.gllanos.securedocs.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "permisos")
public class Permiso {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    @Enumerated(EnumType.STRING)
    private PermisoNombre nombre;

    public enum PermisoNombre {
        CREAR_DOCUMENTO,
        CONSULTAR_DOCUMENTO,
        MODIFICAR_DOCUMENTO,
        ELIMINAR_DOCUMENTO,
        APROBAR_DOCUMENTO,
        VER_AUDITORIA,
        GESTIONAR_USUARIOS,
        ASIGNAR_ROLES
    }
}