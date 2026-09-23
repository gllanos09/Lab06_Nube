package com.gllanos.securedocs.controller;

import com.gllanos.securedocs.abac.EntornoRequest;
import com.gllanos.securedocs.model.Documento;
import com.gllanos.securedocs.model.Usuario;
import com.gllanos.securedocs.repository.UsuarioRepository;
import com.gllanos.securedocs.service.DocumentoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/documentos")
@RequiredArgsConstructor
public class DocumentoController {

    private final DocumentoService documentoService;
    private final UsuarioRepository usuarioRepository;

    private Usuario getUsuario(Authentication auth) {
        return usuarioRepository.findByCorreo(auth.getName())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
    }

    @GetMapping
    public ResponseEntity<List<Documento>> listar(
            Authentication auth,
            @RequestBody(required = false) EntornoRequest entorno) {
        if (entorno == null) entorno = new EntornoRequest();
        return ResponseEntity.ok(documentoService.listar(getUsuario(auth), entorno));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Documento> obtener(
            @PathVariable Long id,
            Authentication auth,
            @RequestBody(required = false) EntornoRequest entorno) {
        if (entorno == null) entorno = new EntornoRequest();
        return ResponseEntity.ok(documentoService.obtener(id, getUsuario(auth), entorno));
    }

    @PostMapping
    public ResponseEntity<Documento> crear(
            @RequestBody Map<String, Object> body,
            Authentication auth) {
        EntornoRequest entorno = extractEntorno(body);
        Documento documento = extractDocumento(body);
        return ResponseEntity.ok(documentoService.crear(documento, getUsuario(auth), entorno));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Documento> modificar(
            @PathVariable Long id,
            @RequestBody Map<String, Object> body,
            Authentication auth) {
        EntornoRequest entorno = extractEntorno(body);
        Documento documento = extractDocumento(body);
        return ResponseEntity.ok(documentoService.modificar(id, documento, getUsuario(auth), entorno));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(
            @PathVariable Long id,
            @RequestBody(required = false) EntornoRequest entorno,
            Authentication auth) {
        if (entorno == null) entorno = new EntornoRequest();
        documentoService.eliminar(id, getUsuario(auth), entorno);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/aprobar")
    public ResponseEntity<Documento> aprobar(
            @PathVariable Long id,
            @RequestBody(required = false) EntornoRequest entorno,
            Authentication auth) {
        if (entorno == null) entorno = new EntornoRequest();
        return ResponseEntity.ok(documentoService.aprobar(id, getUsuario(auth), entorno));
    }

    private EntornoRequest extractEntorno(Map<String, Object> body) {
        EntornoRequest entorno = new EntornoRequest();
        if (body.containsKey("entorno")) {
            @SuppressWarnings("unchecked")
            Map<String, String> e = (Map<String, String>) body.get("entorno");
            entorno.setHora(e.get("hora"));
            entorno.setDireccionIp(e.get("direccionIp"));
            entorno.setUbicacion(e.get("ubicacion"));
            entorno.setDispositivo(e.get("dispositivo"));
        }
        return entorno;
    }

    private Documento extractDocumento(Map<String, Object> body) {
        Documento documento = new Documento();
        if (body.containsKey("titulo"))
            documento.setTitulo((String) body.get("titulo"));
        if (body.containsKey("descripcion"))
            documento.setDescripcion((String) body.get("descripcion"));
        if (body.containsKey("nivelConfidencialidad"))
            documento.setNivelConfidencialidad((Integer) body.get("nivelConfidencialidad"));
        if (body.containsKey("estado"))
            documento.setEstado(Documento.EstadoDocumento.valueOf((String) body.get("estado")));
        if (body.containsKey("pais"))
            documento.setPais((String) body.get("pais"));
        return documento;
    }
}