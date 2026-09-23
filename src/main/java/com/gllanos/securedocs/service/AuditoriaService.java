package com.gllanos.securedocs.service;

import com.gllanos.securedocs.model.Auditoria;
import com.gllanos.securedocs.repository.AuditoriaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AuditoriaService {

    private final AuditoriaRepository auditoriaRepository;

    public void registrar(String usuario, String recurso, String accion,
                          Auditoria.ResultadoAcceso resultado, String motivo,
                          String ip, String dispositivo, String ubicacion) {
        Auditoria auditoria = new Auditoria();
        auditoria.setUsuario(usuario);
        auditoria.setRecurso(recurso);
        auditoria.setAccion(accion);
        auditoria.setResultado(resultado);
        auditoria.setMotivo(motivo);
        auditoria.setDireccionIp(ip);
        auditoria.setDispositivo(dispositivo);
        auditoria.setUbicacion(ubicacion);
        auditoriaRepository.save(auditoria);
    }

    public List<Auditoria> listarTodo() {
        return auditoriaRepository.findAllByOrderByFechaDesc();
    }
}