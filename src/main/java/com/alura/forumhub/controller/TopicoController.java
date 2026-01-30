package com.alura.forumhub.controller;

import com.alura.forumhub.domain.curso.Curso;
import com.alura.forumhub.domain.topico.Topico;
import com.alura.forumhub.domain.usuario.Usuario;
import com.alura.forumhub.dto.topico.DadosAtualizacaoTopico;
import com.alura.forumhub.dto.topico.DadosCadastroTopico;
import com.alura.forumhub.dto.topico.DadosDetalhamentoTopico;
import com.alura.forumhub.dto.topico.DadosListagemTopico;
import com.alura.forumhub.infra.exception.ValidacaoException;
import com.alura.forumhub.repository.CursoRepository;
import com.alura.forumhub.repository.TopicoRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("/topicos")
@SecurityRequirement(name = "bearer-key")
@Tag(name = "Tópicos", description = "Gerenciamento de tópicos do fórum")
public class TopicoController {

    @Autowired
    private TopicoRepository topicoRepository;

    @Autowired
    private CursoRepository cursoRepository;

    @PostMapping
    @Transactional
    @Operation(summary = "Cadastrar um novo tópico", description = "Cria um novo tópico no fórum")
    public ResponseEntity<DadosDetalhamentoTopico> cadastrar(
            @RequestBody @Valid DadosCadastroTopico dados,
            @AuthenticationPrincipal Usuario usuarioLogado,
            UriComponentsBuilder uriBuilder) {

        // Validar se já existe tópico com mesmo título e mensagem
        if (topicoRepository.existsByTituloAndMensagem(dados.titulo(), dados.mensagem())) {
            throw new ValidacaoException("Já existe um tópico com este título e mensagem");
        }

        // Buscar o curso
        Curso curso = cursoRepository.findById(dados.cursoId())
                .orElseThrow(() -> new ValidacaoException("Curso não encontrado"));

        // Criar o tópico
        Topico topico = new Topico(dados.titulo(), dados.mensagem(), usuarioLogado, curso);
        topicoRepository.save(topico);

        var uri = uriBuilder.path("/topicos/{id}").buildAndExpand(topico.getId()).toUri();
        return ResponseEntity.created(uri).body(new DadosDetalhamentoTopico(topico));
    }

    @GetMapping
    @Operation(summary = "Listar todos os tópicos", description = "Retorna uma lista paginada de todos os tópicos")
    public ResponseEntity<Page<DadosListagemTopico>> listar(
            @PageableDefault(size = 10, sort = {"dataCriacao"}) Pageable paginacao,
            @RequestParam(required = false) String curso,
            @RequestParam(required = false) Integer ano) {

        Page<Topico> page;

        if (curso != null && ano != null) {
            page = topicoRepository.findByCursoNomeAndAno(curso, ano, paginacao);
        } else if (curso != null) {
            page = topicoRepository.findByCursoNome(curso, paginacao);
        } else if (ano != null) {
            page = topicoRepository.findByAno(ano, paginacao);
        } else {
            page = topicoRepository.findAllByOrderByDataCriacaoDesc(paginacao);
        }

        return ResponseEntity.ok(page.map(DadosListagemTopico::new));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Detalhar um tópico", description = "Retorna os detalhes de um tópico específico")
    public ResponseEntity<DadosDetalhamentoTopico> detalhar(@PathVariable Long id) {
        Topico topico = topicoRepository.findByIdWithRespostas(id)
                .orElseThrow(() -> new ValidacaoException("Tópico não encontrado"));

        return ResponseEntity.ok(new DadosDetalhamentoTopico(topico));
    }

    @PutMapping("/{id}")
    @Transactional
    @Operation(summary = "Atualizar um tópico", description = "Atualiza os dados de um tópico existente")
    public ResponseEntity<DadosDetalhamentoTopico> atualizar(
            @PathVariable Long id,
            @RequestBody @Valid DadosAtualizacaoTopico dados,
            @AuthenticationPrincipal Usuario usuarioLogado) {

        Topico topico = topicoRepository.findById(id)
                .orElseThrow(() -> new ValidacaoException("Tópico não encontrado"));

        // Verificar se o usuário é o autor do tópico
        if (!topico.getAutor().getId().equals(usuarioLogado.getId())) {
            throw new ValidacaoException("Você não tem permissão para editar este tópico");
        }

        // Verificar duplicidade se título ou mensagem foram alterados
        if (dados.titulo() != null || dados.mensagem() != null) {
            String novoTitulo = dados.titulo() != null ? dados.titulo() : topico.getTitulo();
            String novaMensagem = dados.mensagem() != null ? dados.mensagem() : topico.getMensagem();

            if (!novoTitulo.equals(topico.getTitulo()) || !novaMensagem.equals(topico.getMensagem())) {
                if (topicoRepository.existsByTituloAndMensagem(novoTitulo, novaMensagem)) {
                    throw new ValidacaoException("Já existe um tópico com este título e mensagem");
                }
            }
        }

        topico.atualizar(dados);
        return ResponseEntity.ok(new DadosDetalhamentoTopico(topico));
    }

    @DeleteMapping("/{id}")
    @Transactional
    @Operation(summary = "Excluir um tópico", description = "Remove um tópico do fórum")
    public ResponseEntity<Void> excluir(
            @PathVariable Long id,
            @AuthenticationPrincipal Usuario usuarioLogado) {

        Topico topico = topicoRepository.findById(id)
                .orElseThrow(() -> new ValidacaoException("Tópico não encontrado"));

        // Verificar se o usuário é o autor do tópico
        if (!topico.getAutor().getId().equals(usuarioLogado.getId())) {
            throw new ValidacaoException("Você não tem permissão para excluir este tópico");
        }

        topicoRepository.delete(topico);
        return ResponseEntity.noContent().build();
    }
}
