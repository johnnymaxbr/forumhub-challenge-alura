package com.alura.forumhub.domain.resposta;

import com.alura.forumhub.domain.topico.Topico;
import com.alura.forumhub.domain.usuario.Usuario;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Table(name = "respostas")
@Entity(name = "Resposta")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class Resposta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "TEXT")
    private String mensagem;

    @Column(name = "data_criacao")
    private LocalDateTime dataCriacao;

    private Boolean solucao;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "autor_id")
    private Usuario autor;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "topico_id")
    private Topico topico;

    public Resposta(String mensagem, Usuario autor, Topico topico) {
        this.mensagem = mensagem;
        this.dataCriacao = LocalDateTime.now();
        this.solucao = false;
        this.autor = autor;
        this.topico = topico;
    }

    public void marcarComoSolucao() {
        this.solucao = true;
    }
}
