package com.gllanos.securedocs.service;

import com.gllanos.securedocs.abac.AbacResult;
import com.gllanos.securedocs.abac.AutorizacionService;
import com.gllanos.securedocs.abac.EntornoRequest;
import com.gllanos.securedocs.model.Auditoria;
import com.gllanos.securedocs.model.Documento;
import com.gllanos.securedocs.model.Permiso;
import com.gllanos.securedocs.model.Usuario;
import com.gllanos.securedocs.repository.DocumentoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DocumentoService {

    private final DocumentoRepository documentoRepository;
    private final AutorizacionService autorizacionService;
    private final AuditoriaService auditoriaService;

    private void verificar(Usuario usuario, Documento documento,
                           Permiso.PermisoNombre permiso, EntornoRequest entorno) {
        AbacResult result = autorizacionService.autorizar(usuario, documento, permiso, entorno);

        auditoriaService.registrar(
                usuario.getCorreo(),
                "documento-" + (documento.getId() != null ? documento.getId() : "nuevo"),
                permiso.name(),
                result.isPermitido() ? Auditoria.ResultadoAcceso.PERMITIDO : Auditoria.ResultadoAcceso.DENEGADO,
                result.getMotivo(),
                entorno.getDireccionIp(),
                entorno.getDispositivo(),
                entorno.getUbicacion()
        );

        if (!result.isPermitido()) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, result.getMotivo());
        }
    }

    public List<Documento> listar(Usuario usuario, EntornoRequest entorno) {
        List<Documento> todos = documentoRepository.findAll();
        return todos.stream()
                .filter(doc -> {
                    AbacResult r = autorizacionService.autorizar(
                            usuario, doc, Permiso.PermisoNombre.CONSULTAR_DOCUMENTO, entorno);
                    return r.isPermitido();
                })
                .toList();
    }

    public Documento obtener(Long id, Usuario usuario, EntornoRequest entorno) {
        Documento documento = documentoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Documento no encontrado"));
        verificar(usuario, documento, Permiso.PermisoNombre.CONSULTAR_DOCUMENTO, entorno);
        return documento;
    }

    public Documento crear(Documento documento, Usuario usuario, EntornoRequest entorno) {
        documento.setPropietario(usuario);
        if (documento.getDepartamento() == null) {
            documento.setDepartamento(usuario.getDepartamento());
        }
        verificar(usuario, documento, Permiso.PermisoNombre.CREAR_DOCUMENTO, entorno);
        return documentoRepository.save(documento);
    }

    public Documento modificar(Long id, Documento datos, Usuario usuario, EntornoRequest entorno) {
        Documento documento = documentoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Documento no encontrado"));
        verificar(usuario, documento, Permiso.PermisoNombre.MODIFICAR_DOCUMENTO, entorno);
        documento.setTitulo(datos.getTitulo());
        documento.setDescripcion(datos.getDescripcion());
        documento.setNivelConfidencialidad(datos.getNivelConfidencialidad());
        documento.setEstado(datos.getEstado());
        return documentoRepository.save(documento);
    }

    public void eliminar(Long id, Usuario usuario, EntornoRequest entorno) {
        Documento documento = documentoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Documento no encontrado"));
        verificar(usuario, documento, Permiso.PermisoNombre.ELIMINAR_DOCUMENTO, entorno);
        documentoRepository.delete(documento);
    }

    public Documento aprobar(Long id, Usuario usuario, EntornoRequest entorno) {
        Documento documento = documentoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Documento no encontrado"));
        verificar(usuario, documento, Permiso.PermisoNombre.APROBAR_DOCUMENTO, entorno);
        documento.setEstado(Documento.EstadoDocumento.APROBADO);
        return documentoRepository.save(documento);
    }
}