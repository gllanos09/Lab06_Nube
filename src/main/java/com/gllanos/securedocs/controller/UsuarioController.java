package com.gllanos.securedocs.controller;

import com.gllanos.securedocs.model.Permiso;
import com.gllanos.securedocs.model.Usuario;
import com.gllanos.securedocs.rbac.RbacService;
import com.gllanos.securedocs.repository.UsuarioRepository;
import com.gllanos.securedocs.service.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/usuarios")
@RequiredArgsConstructor
public class UsuarioController {

    private final UsuarioService usuarioService;
    private final UsuarioRepository usuarioRepository;
    private final RbacService rbacService;

    private Usuario getUsuarioAutenticado(Authentication auth) {
        return usuarioRepository.findByCorreo(auth.getName())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.UNAUTHORIZED, "No autenticado"));
    }

    private void verificarPermiso(Authentication auth, Permiso.PermisoNombre permiso) {
        Usuario usuario = getUsuarioAutenticado(auth);
        if (!rbacService.tienePermiso(usuario, permiso)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN,
                    "No tiene permiso: " + permiso.name());
        }
    }

    @GetMapping
    public ResponseEntity<List<Usuario>> listar(Authentication auth) {
        verificarPermiso(auth, Permiso.PermisoNombre.GESTIONAR_USUARIOS);
        return ResponseEntity.ok(usuarioService.listar());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Usuario> obtener(@PathVariable Long id, Authentication auth) {
        verificarPermiso(auth, Permiso.PermisoNombre.GESTIONAR_USUARIOS);
        return ResponseEntity.ok(usuarioService.obtener(id));
    }

    @PostMapping
    public ResponseEntity<Usuario> crear(@RequestBody Usuario usuario, Authentication auth) {
        verificarPermiso(auth, Permiso.PermisoNombre.GESTIONAR_USUARIOS);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(usuarioService.crear(usuario));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Usuario> modificar(@PathVariable Long id,
                                             @RequestBody Usuario datos,
                                             Authentication auth) {
        verificarPermiso(auth, Permiso.PermisoNombre.GESTIONAR_USUARIOS);
        return ResponseEntity.ok(usuarioService.modificar(id, datos));
    }

    @PatchMapping("/{id}/activar")
    public ResponseEntity<Void> activar(@PathVariable Long id, Authentication auth) {
        verificarPermiso(auth, Permiso.PermisoNombre.GESTIONAR_USUARIOS);
        usuarioService.activar(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/desactivar")
    public ResponseEntity<Void> desactivar(@PathVariable Long id, Authentication auth) {
        verificarPermiso(auth, Permiso.PermisoNombre.GESTIONAR_USUARIOS);
        usuarioService.desactivar(id);
        return ResponseEntity.noContent().build();
    }
}