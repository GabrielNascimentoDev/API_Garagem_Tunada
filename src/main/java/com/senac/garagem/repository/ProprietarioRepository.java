package com.senac.garagem.repository;

import com.senac.garagem.model.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProprietarioRepository extends JpaRepository<Proprietario, Long> {

    Page<Proprietario> findByNomeContainingIgnoreCase(String nome, Pageable pageable);
}
