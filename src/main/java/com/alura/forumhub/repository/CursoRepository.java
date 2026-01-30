package com.alura.forumhub.repository;

import com.alura.forumhub.domain.curso.Categoria;
import com.alura.forumhub.domain.curso.Curso;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CursoRepository extends JpaRepository<Curso, Long> {

    Optional<Curso> findByNomeIgnoreCase(String nome);

    List<Curso> findByCategoria(Categoria categoria);

    List<Curso> findByAtivoTrue();

    boolean existsByNomeIgnoreCase(String nome);
}
