package com.gllanos.securedocs.repository;

import com.gllanos.securedocs.model.Documento;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DocumentoRepository extends JpaRepository<Documento, Long> {
    List<Documento> findByDepartamentoId(Long departamentoId);
}