package com.alura.forumhub.repository;

import com.alura.forumhub.domain.topico.StatusTopico;
import com.alura.forumhub.domain.topico.Topico;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TopicoRepository extends JpaRepository<Topico, Long> {

    boolean existsByTituloAndMensagem(String titulo, String mensagem);

    Page<Topico> findAllByOrderByDataCriacaoDesc(Pageable pageable);

    @Query("SELECT t FROM Topico t WHERE t.curso.nome = :nomeCurso ORDER BY t.dataCriacao DESC")
    Page<Topico> findByCursoNome(@Param("nomeCurso") String nomeCurso, Pageable pageable);

    @Query("SELECT t FROM Topico t WHERE YEAR(t.dataCriacao) = :ano ORDER BY t.dataCriacao DESC")
    Page<Topico> findByAno(@Param("ano") Integer ano, Pageable pageable);

    @Query("SELECT t FROM Topico t WHERE t.curso.nome = :nomeCurso AND YEAR(t.dataCriacao) = :ano ORDER BY t.dataCriacao DESC")
    Page<Topico> findByCursoNomeAndAno(@Param("nomeCurso") String nomeCurso, @Param("ano") Integer ano, Pageable pageable);

    List<Topico> findByStatus(StatusTopico status);

    @Query("SELECT t FROM Topico t WHERE t.autor.id = :autorId ORDER BY t.dataCriacao DESC")
    Page<Topico> findByAutorId(@Param("autorId") Long autorId, Pageable pageable);

    @Query("SELECT t FROM Topico t LEFT JOIN FETCH t.respostas WHERE t.id = :id")
    Optional<Topico> findByIdWithRespostas(@Param("id") Long id);
}
