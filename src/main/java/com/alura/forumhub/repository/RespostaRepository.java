package com.alura.forumhub.repository;

import com.alura.forumhub.domain.resposta.Resposta;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RespostaRepository extends JpaRepository<Resposta, Long> {

    Page<Resposta> findByTopicoId(Long topicoId, Pageable pageable);

    List<Resposta> findByTopicoIdOrderByDataCriacaoAsc(Long topicoId);

    List<Resposta> findByAutorId(Long autorId);
}
