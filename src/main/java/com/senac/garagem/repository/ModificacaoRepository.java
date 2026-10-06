package com.senac.garagem.repository;

import com.senac.garagem.model.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ModificacaoRepository extends JpaRepository<Modificacao, Long> {

    Page<Modificacao> findByCategoria(CategoriaModificacao categoria, Pageable pageable);
}
