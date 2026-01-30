package com.alura.forumhub.dto.topico;

import com.alura.forumhub.domain.topico.StatusTopico;
import com.alura.forumhub.domain.topico.Topico;

import java.time.LocalDateTime;

public record DadosDetalhamentoTopico(
        Long id,
        String titulo,
        String mensagem,
        LocalDateTime dataCriacao,
        StatusTopico status,
        Long autorId,
        String autorNome,
        String autorEmail,
        Long cursoId,
        String cursoNome,
        Integer quantidadeRespostas
) {
    public DadosDetalhamentoTopico(Topico topico) {
        this(
                topico.getId(),
                topico.getTitulo(),
                topico.getMensagem(),
                topico.getDataCriacao(),
                topico.getStatus(),
                topico.getAutor().getId(),
                topico.getAutor().getNome(),
                topico.getAutor().getEmail(),
                topico.getCurso().getId(),
                topico.getCurso().getNome(),
                topico.getRespostas() != null ? topico.getRespostas().size() : 0
        );
    }
}
