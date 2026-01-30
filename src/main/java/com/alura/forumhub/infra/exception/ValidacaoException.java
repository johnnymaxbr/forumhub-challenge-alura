package com.alura.forumhub.infra.exception;

public class ValidacaoException extends RuntimeException {

    public ValidacaoException(String mensagem) {
        super(mensagem);
    }
}
