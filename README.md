# Fórum Hub - API REST

![Java](https://img.shields.io/badge/Java-17-orange?style=for-the-badge&logo=openjdk&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.2.3-brightgreen?style=for-the-badge&logo=spring&logoColor=white)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-16-blue?style=for-the-badge&logo=postgresql&logoColor=white)
![JWT](https://img.shields.io/badge/JWT-Auth-red?style=for-the-badge&logo=jsonwebtokens&logoColor=white)
![Swagger](https://img.shields.io/badge/Swagger-OpenAPI-85EA2D?style=for-the-badge&logo=swagger&logoColor=black)

## Descrição

**Fórum Hub** é uma API REST desenvolvida como parte do desafio **Challenge Back End** da [Alura](https://www.alura.com.br/) em parceria com o programa **Oracle Next Education (ONE)**. O projeto simula o funcionamento de um fórum de discussões, onde usuários podem criar, visualizar, atualizar e excluir tópicos.

A API foi desenvolvida seguindo as melhores práticas de desenvolvimento, incluindo autenticação via JWT, validações de regras de negócio e documentação interativa com Swagger.

## Funcionalidades

- [x] **Cadastro de tópicos** - Criar novos tópicos no fórum
- [x] **Listagem de tópicos** - Visualizar todos os tópicos com paginação
- [x] **Detalhamento de tópico** - Visualizar detalhes de um tópico específico
- [x] **Atualização de tópico** - Editar tópicos existentes
- [x] **Exclusão de tópico** - Remover tópicos do fórum
- [x] **Autenticação JWT** - Login seguro com tokens
- [x] **Registro de usuários** - Criar novas contas
- [x] **Documentação Swagger** - API documentada e interativa

## Tecnologias Utilizadas

| Tecnologia | Versão | Descrição |
|------------|--------|-----------|
| Java | 17 | Linguagem de programação |
| Spring Boot | 3.2.3 | Framework principal |
| Spring Security | 6.x | Segurança e autenticação |
| Spring Data JPA | 3.x | Persistência de dados |
| PostgreSQL | 16 | Banco de dados relacional |
| JWT (Auth0) | 4.4.0 | Tokens de autenticação |
| SpringDoc OpenAPI | 2.3.0 | Documentação Swagger |
| Lombok | 1.18.x | Redução de boilerplate |
| Maven | 3.9+ | Gerenciador de dependências |

## Arquitetura do Projeto

```
src/main/java/com/alura/forumhub/
├── ForumHubApplication.java          # Classe principal
├── controller/
│   ├── AutenticacaoController.java   # Endpoints de autenticação
│   └── TopicoController.java         # Endpoints de tópicos (CRUD)
├── domain/
│   ├── curso/
│   │   ├── Categoria.java            # Enum de categorias
│   │   └── Curso.java                # Entidade Curso
│   ├── resposta/
│   │   └── Resposta.java             # Entidade Resposta
│   ├── topico/
│   │   ├── StatusTopico.java         # Enum de status
│   │   └── Topico.java               # Entidade Tópico
│   └── usuario/
│       ├── Perfil.java               # Enum de perfis
│       └── Usuario.java              # Entidade Usuário
├── dto/
│   ├── autenticacao/                 # DTOs de autenticação
│   ├── topico/                       # DTOs de tópicos
│   └── usuario/                      # DTOs de usuários
├── infra/
│   ├── exception/                    # Tratamento de exceções
│   ├── security/                     # Configurações de segurança
│   └── springdoc/                    # Configurações do Swagger
└── repository/                       # Repositórios JPA
```

## Endpoints da API

### Autenticação

| Método | Endpoint | Descrição | Autenticação |
|--------|----------|-----------|--------------|
| POST | `/auth/login` | Realizar login | Não |
| POST | `/auth/registrar` | Registrar novo usuário | Não |

### Tópicos

| Método | Endpoint | Descrição | Autenticação |
|--------|----------|-----------|--------------|
| GET | `/topicos` | Listar todos os tópicos | Não |
| GET | `/topicos/{id}` | Detalhar um tópico | Não |
| POST | `/topicos` | Criar novo tópico | Sim (JWT) |
| PUT | `/topicos/{id}` | Atualizar tópico | Sim (JWT) |
| DELETE | `/topicos/{id}` | Excluir tópico | Sim (JWT) |

## Regras de Negócio

1. **Tópicos duplicados**: A API não permite o cadastro de tópicos com o mesmo título e mensagem.
2. **Campos obrigatórios**: Título, mensagem e curso são obrigatórios para criar um tópico.
3. **Permissões**: Apenas o autor de um tópico pode editá-lo ou excluí-lo.
4. **Validação de email**: O email deve ter formato válido e ser único no sistema.

## Pré-requisitos

- Java 17 ou superior
- Maven 3.9 ou superior
- PostgreSQL 12 ou superior

## Configuração do Banco de Dados

1. Crie um banco de dados PostgreSQL:

```sql
CREATE DATABASE forumhub;
```

2. Configure as variáveis de ambiente ou edite o arquivo `application.properties`:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/forumhub
spring.datasource.username=postgres
spring.datasource.password=postgres
```

### Variáveis de Ambiente

| Variável | Descrição | Valor Padrão |
|----------|-----------|--------------|
| `DB_HOST` | Host do banco de dados | localhost |
| `DB_PORT` | Porta do banco de dados | 5432 |
| `DB_NAME` | Nome do banco de dados | forumhub |
| `DB_USER` | Usuário do banco | postgres |
| `DB_PASSWORD` | Senha do banco | postgres |
| `JWT_SECRET` | Chave secreta do JWT | forumhub-secret-key-alura-challenge-2024 |
| `JWT_EXPIRATION` | Tempo de expiração do token (horas) | 2 |

## Como Executar

1. Clone o repositório:

```bash
git clone https://github.com/SEU_USUARIO/forumhub-challenge-alura.git
cd forumhub-challenge-alura
```

2. Configure o banco de dados PostgreSQL

3. Compile o projeto:

```bash
mvn clean compile
```

4. Execute a aplicação:

```bash
mvn spring-boot:run
```

5. Acesse a documentação Swagger:

```
http://localhost:8080/swagger-ui.html
```

## Exemplos de Uso

### Registrar um novo usuário

```bash
curl -X POST http://localhost:8080/auth/registrar \
  -H "Content-Type: application/json" \
  -d '{
    "nome": "João Silva",
    "email": "joao@email.com",
    "senha": "123456"
  }'
```

### Realizar login

```bash
curl -X POST http://localhost:8080/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "email": "joao@email.com",
    "senha": "123456"
  }'
```

### Criar um tópico

```bash
curl -X POST http://localhost:8080/topicos \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer SEU_TOKEN_JWT" \
  -d '{
    "titulo": "Dúvida sobre Spring Boot",
    "mensagem": "Como configurar o Spring Security?",
    "cursoId": 1
  }'
```

### Listar tópicos

```bash
curl -X GET "http://localhost:8080/topicos?page=0&size=10"
```

## Modelo do Banco de Dados

```
┌─────────────────┐       ┌─────────────────┐
│    USUARIOS     │       │     CURSOS      │
├─────────────────┤       ├─────────────────┤
│ id (PK)         │       │ id (PK)         │
│ nome            │       │ nome            │
│ email (UNIQUE)  │       │ categoria       │
│ senha           │       │ ativo           │
│ perfil          │       └────────┬────────┘
│ ativo           │                │
└────────┬────────┘                │
         │                         │
         │    ┌────────────────────┤
         │    │                    │
         ▼    ▼                    ▼
┌─────────────────────────────────────────┐
│               TOPICOS                    │
├─────────────────────────────────────────┤
│ id (PK)                                  │
│ titulo                                   │
│ mensagem                                 │
│ data_criacao                             │
│ status                                   │
│ autor_id (FK) ──────────────────────────►│
│ curso_id (FK) ──────────────────────────►│
└─────────────────────────────────────────┘
         │
         │
         ▼
┌─────────────────┐
│    RESPOSTAS    │
├─────────────────┤
│ id (PK)         │
│ mensagem        │
│ data_criacao    │
│ solucao         │
│ autor_id (FK)   │
│ topico_id (FK)  │
└─────────────────┘
```

## Documentação da API

A documentação completa da API está disponível através do Swagger UI:

- **Swagger UI**: `http://localhost:8080/swagger-ui.html`
- **OpenAPI JSON**: `http://localhost:8080/api-docs`

## Testes

Para executar os testes:

```bash
mvn test
```

## Contribuição

Este projeto foi desenvolvido como parte do programa ONE (Oracle Next Education) em parceria com a Alura. Contribuições são bem-vindas!

## Licença

Este projeto está sob a licença Apache 2.0. Veja o arquivo [LICENSE](LICENSE) para mais detalhes.

---

Desenvolvido com ❤️ como parte do **Challenge Back End** da Alura ONE.
