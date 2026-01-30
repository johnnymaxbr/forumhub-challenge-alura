package com.alura.forumhub.domain.topico;

import com.alura.forumhub.domain.curso.Curso;
import com.alura.forumhub.domain.resposta.Resposta;
import com.alura.forumhub.domain.usuario.Usuario;
import com.alura.forumhub.dto.topico.DadosAtualizacaoTopico;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Table(name = "topicos")
@Entity(name = "Topico")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class Topico {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String titulo;

    @Column(columnDefinition = "TEXT")
    private String mensagem;

    @Column(name = "data_criacao")
    private LocalDateTime dataCriacao;

    @Enumerated(EnumType.STRING)
    private StatusTopico status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "autor_id")
    private Usuario autor;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "curso_id")
    private Curso curso;

    @OneToMany(mappedBy = "topico", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Resposta> respostas = new ArrayList<>();

    public Topico(String titulo, String mensagem, Usuario autor, Curso curso) {
        this.titulo = titulo;
        this.mensagem = mensagem;
        this.dataCriacao = LocalDateTime.now();
        this.status = StatusTopico.NAO_RESPONDIDO;
        this.autor = autor;
        this.curso = curso;
    }

    public void atualizar(DadosAtualizacaoTopico dados) {
        if (dados.titulo() != null) {
            this.titulo = dados.titulo();
        }
        if (dados.mensagem() != null) {
            this.mensagem = dados.mensagem();
        }
        if (dados.status() != null) {
            this.status = dados.status();
        }
    }

    public void fechar() {
        this.status = StatusTopico.FECHADO;
    }

    public void marcarComoSolucionado() {
        this.status = StatusTopico.SOLUCIONADO;
    }
}
