package com.alura.forumhub.controller;

import com.alura.forumhub.domain.usuario.Usuario;
import com.alura.forumhub.dto.autenticacao.DadosAutenticacao;
import com.alura.forumhub.dto.autenticacao.DadosTokenJWT;
import com.alura.forumhub.dto.usuario.DadosCadastroUsuario;
import com.alura.forumhub.infra.exception.ValidacaoException;
import com.alura.forumhub.infra.security.TokenService;
import com.alura.forumhub.repository.UsuarioRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@Tag(name = "Autenticação", description = "Endpoints para autenticação e registro de usuários")
public class AutenticacaoController {

    @Autowired
    private AuthenticationManager manager;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/login")
    @Operation(summary = "Realizar login", description = "Autentica um usuário e retorna um token JWT")
    public ResponseEntity<DadosTokenJWT> login(@RequestBody @Valid DadosAutenticacao dados) {
        var authenticationToken = new UsernamePasswordAuthenticationToken(dados.email(), dados.senha());
        var authentication = manager.authenticate(authenticationToken);

        var tokenJWT = tokenService.gerarToken((Usuario) authentication.getPrincipal());

        return ResponseEntity.ok(new DadosTokenJWT(tokenJWT));
    }

    @PostMapping("/registrar")
    @Transactional
    @Operation(summary = "Registrar novo usuário", description = "Cria uma nova conta de usuário")
    public ResponseEntity<Void> registrar(@RequestBody @Valid DadosCadastroUsuario dados) {
        if (usuarioRepository.existsByEmail(dados.email())) {
            throw new ValidacaoException("Email já cadastrado");
        }

        String senhaCriptografada = passwordEncoder.encode(dados.senha());
        Usuario usuario = new Usuario(dados.nome(), dados.email(), senhaCriptografada);
        usuarioRepository.save(usuario);

        return ResponseEntity.ok().build();
    }
}
