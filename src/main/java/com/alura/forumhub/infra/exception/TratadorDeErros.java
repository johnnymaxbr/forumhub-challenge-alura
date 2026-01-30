package com.alura.forumhub.infra.exception;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

@RestControllerAdvice
public class TratadorDeErros {

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<Void> tratarErro404() {
        return ResponseEntity.notFound().build();
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<List<DadosErroValidacao>> tratarErro400(MethodArgumentNotValidException ex) {
        var erros = ex.getFieldErrors();
        return ResponseEntity.badRequest().body(erros.stream().map(DadosErroValidacao::new).toList());
    }

    @ExceptionHandler(ValidacaoException.class)
    public ResponseEntity<DadosErro> tratarErroRegraDeNegocio(ValidacaoException ex) {
        return ResponseEntity.badRequest().body(new DadosErro(ex.getMessage()));
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<DadosErro> tratarErroBadCredentials() {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(new DadosErro("Credenciais inválidas"));
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<DadosErro> tratarErroAuthentication() {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(new DadosErro("Falha na autenticação"));
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<DadosErro> tratarErroAcessoNegado() {
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(new DadosErro("Acesso negado"));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<DadosErro> tratarErro500(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new DadosErro("Erro interno do servidor: " + ex.getLocalizedMessage()));
    }

    private record DadosErroValidacao(String campo, String mensagem) {
        public DadosErroValidacao(FieldError erro) {
            this(erro.getField(), erro.getDefaultMessage());
        }
    }

    private record DadosErro(String mensagem) {
    }
}
