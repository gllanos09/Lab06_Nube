package com.gllanos.securedocs.controller;

import com.gllanos.securedocs.model.Auditoria;
import com.gllanos.securedocs.model.Permiso;
import com.gllanos.securedocs.model.Usuario;
import com.gllanos.securedocs.rbac.RbacService;
import com.gllanos.securedocs.repository.UsuarioRepository;
import com.gllanos.securedocs.service.AuditoriaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/auditoria")
@RequiredArgsConstructor
public class AuditoriaController {

    private final AuditoriaService auditoriaService;
    private final UsuarioRepository usuarioRepository;
    private final RbacService rbacService;

    @GetMapping
    public ResponseEntity<List<Auditoria>> listar(Authentication auth) {
        Usuario usuario = usuarioRepository.findByCorreo(auth.getName())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED));

        if (!rbacService.tienePermiso(usuario, Permiso.PermisoNombre.VER_AUDITORIA)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN,
                    "No tiene permiso para ver la auditoría");
        }

        return ResponseEntity.ok(auditoriaService.listarTodo());
    }
}