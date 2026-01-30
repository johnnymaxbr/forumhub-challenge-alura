package com.alura.forumhub.infra.security;

import com.alura.forumhub.domain.usuario.Usuario;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

@Service
public class TokenService {

    @Value("${api.security.token.secret}")
    private String secret;

    @Value("${api.security.token.expiration}")
    private Integer expiration;

    public String gerarToken(Usuario usuario) {
        try {
            var algoritmo = Algorithm.HMAC256(secret);
            return JWT.create()
                    .withIssuer("API Forum Hub")
                    .withSubject(usuario.getEmail())
                    .withClaim("id", usuario.getId())
                    .withClaim("nome", usuario.getNome())
                    .withExpiresAt(dataExpiracao())
                    .sign(algoritmo);
        } catch (JWTCreationException exception) {
            throw new RuntimeException("Erro ao gerar token JWT", exception);
        }
    }

    public String getSubject(String tokenJWT) {
        try {
            var algoritmo = Algorithm.HMAC256(secret);
            return JWT.require(algoritmo)
                    .withIssuer("API Forum Hub")
                    .build()
                    .verify(tokenJWT)
                    .getSubject();
        } catch (JWTVerificationException exception) {
            throw new RuntimeException("Token JWT inválido ou expirado");
        }
    }

    private Instant dataExpiracao() {
        return LocalDateTime.now()
                .plusHours(expiration)
                .atZone(ZoneId.systemDefault())
                .toInstant();
    }
}
