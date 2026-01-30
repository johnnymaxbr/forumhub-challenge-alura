package com.alura.forumhub.dto.topico;

import com.alura.forumhub.domain.topico.StatusTopico;

public record DadosAtualizacaoTopico(
        String titulo,
        String mensagem,
        StatusTopico status
) {
}
