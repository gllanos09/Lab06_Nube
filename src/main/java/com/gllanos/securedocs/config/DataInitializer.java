package com.gllanos.securedocs.config;

import com.gllanos.securedocs.model.*;
import com.gllanos.securedocs.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final RolRepository rolRepository;
    private final PermisoRepository permisoRepository;
    private final RolPermisoRepository rolPermisoRepository;
    private final UsuarioRepository usuarioRepository;
    private final DepartamentoRepository departamentoRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        if (rolRepository.count() > 0) return;

        // Crear roles
        for (Rol.RolNombre nombre : Rol.RolNombre.values()) {
            Rol rol = new Rol();
            rol.setNombre(nombre);
            rolRepository.save(rol);
        }

        // Crear permisos
        for (Permiso.PermisoNombre nombre : Permiso.PermisoNombre.values()) {
            Permiso permiso = new Permiso();
            permiso.setNombre(nombre);
            permisoRepository.save(permiso);
        }

        // Matriz RBAC
        Map<Rol.RolNombre, List<Permiso.PermisoNombre>> matriz = Map.of(
                Rol.RolNombre.ADMINISTRADOR, List.of(
                        Permiso.PermisoNombre.CREAR_DOCUMENTO,
                        Permiso.PermisoNombre.CONSULTAR_DOCUMENTO,
                        Permiso.PermisoNombre.MODIFICAR_DOCUMENTO,
                        Permiso.PermisoNombre.ELIMINAR_DOCUMENTO,
                        Permiso.PermisoNombre.APROBAR_DOCUMENTO,
                        Permiso.PermisoNombre.VER_AUDITORIA,
                        Permiso.PermisoNombre.GESTIONAR_USUARIOS,
                        Permiso.PermisoNombre.ASIGNAR_ROLES
                ),
                Rol.RolNombre.GERENTE, List.of(
                        Permiso.PermisoNombre.CREAR_DOCUMENTO,
                        Permiso.PermisoNombre.CONSULTAR_DOCUMENTO,
                        Permiso.PermisoNombre.MODIFICAR_DOCUMENTO,
                        Permiso.PermisoNombre.ELIMINAR_DOCUMENTO,
                        Permiso.PermisoNombre.APROBAR_DOCUMENTO,
                        Permiso.PermisoNombre.VER_AUDITORIA
                ),
                Rol.RolNombre.SUPERVISOR, List.of(
                        Permiso.PermisoNombre.CREAR_DOCUMENTO,
                        Permiso.PermisoNombre.CONSULTAR_DOCUMENTO,
                        Permiso.PermisoNombre.MODIFICAR_DOCUMENTO,
                        Permiso.PermisoNombre.APROBAR_DOCUMENTO
                ),
                Rol.RolNombre.EMPLEADO, List.of(
                        Permiso.PermisoNombre.CREAR_DOCUMENTO,
                        Permiso.PermisoNombre.CONSULTAR_DOCUMENTO,
                        Permiso.PermisoNombre.MODIFICAR_DOCUMENTO
                ),
                Rol.RolNombre.AUDITOR, List.of(
                        Permiso.PermisoNombre.CONSULTAR_DOCUMENTO,
                        Permiso.PermisoNombre.VER_AUDITORIA
                ),
                Rol.RolNombre.INVITADO, List.of(
                        Permiso.PermisoNombre.CONSULTAR_DOCUMENTO
                )
        );

        // Persistir la matriz
        matriz.forEach((rolNombre, permisos) -> {
            Rol rol = rolRepository.findByNombre(rolNombre).orElseThrow();
            permisos.forEach(permisoNombre -> {
                Permiso permiso = permisoRepository.findByNombre(permisoNombre).orElseThrow();
                RolPermiso rolPermiso = new RolPermiso();
                rolPermiso.setRol(rol);
                rolPermiso.setPermiso(permiso);
                rolPermisoRepository.save(rolPermiso);
            });
        });

        System.out.println(">>> Roles, permisos y matriz RBAC inicializados.");

        // Crear usuario administrador inicial
        Departamento depto = new Departamento();
        depto.setNombre("SISTEMAS");
        departamentoRepository.save(depto);

        Rol rolAdmin = rolRepository.findByNombre(Rol.RolNombre.ADMINISTRADOR).orElseThrow();

        Usuario admin = new Usuario();
        admin.setNombre("Administrador");
        admin.setCorreo("admin@securedocs.com");
        admin.setPassword(passwordEncoder.encode("admin123"));
        admin.setRol(rolAdmin);
        admin.setDepartamento(depto);
        admin.setNivelSeguridad(5);
        admin.setPais("PERU");
        admin.setTipoContrato(Usuario.TipoContrato.INTERNO);
        admin.setEstado(Usuario.EstadoUsuario.ACTIVO);
        usuarioRepository.save(admin);

        System.out.println(">>> Usuario admin creado: admin@securedocs.com / admin123");
    }
}