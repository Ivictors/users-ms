# User-MS

Microsserviço de gerenciamento de usuários para plataforma de delivery de comida, desenvolvido com Spring Boot e arquitetura orientada a eventos.

## Descrição

O **User-MS** é um componente de uma arquitetura baseada em microsserviços para uma plataforma de delivery de comida. O sistema foi projetado de forma robusta e altamente desacoplada, utilizando Event-Driven Architecture para garantir escalabilidade e comunicação assíncrona entre serviços.

A segurança e o controle de acesso são implementados seguindo as melhores práticas de desenvolvimento de software, utilizando JWT (JSON Web Tokens) com criptografia RSA para autenticação e autorização baseada em roles.

## Tecnologias

- **Java 21**
- **Spring Boot 4.0.4**
- **Spring Security** - Autenticação e autorização
- **Spring Data JPA** - Persistência de dados
- **Spring AMQP** - Comunicação assíncrona com RabbitMQ
- **MySQL 8.0** - Banco de dados relacional
- **OAuth2 Resource Server** - Gerenciamento de tokens JWT
- **SpringDoc OpenAPI** - Documentação automática da API
- **Bean Validation** - Validação de dados

## Arquitetura

```
┌─────────────────────────────────────────────────────┐
│                   User-MS                           │
├─────────────────────────────────────────────────────┤
│  Controllers (REST API)                             │
│  ├── UserController   - CRUD de usuários            │
│  └── LoginController  - Autenticação                │
├─────────────────────────────────────────────────────┤
│  Services                                          │
│  ├── UserService      - Lógica de negócio usuários  │
│  └── LoginService     - Lógica de autenticação      │
├─────────────────────────────────────────────────────┤
│  Repository (Spring Data JPA)                       │
│  ├── UserRepository                                  │
│  └── RoleRepository                                  │
├─────────────────────────────────────────────────────┤
│  Models & DTOs                                      │
│  ├── UserModel, Role                                │
│  └── UserRequestDto, UserResponseDto, LoginDto      │
└─────────────────────────────────────────────────────┘
```

## Funcionalidades

### Gestão de Usuários
- Cadastro de novos usuários
- Listagem de usuários (admin)
- Busca de usuário por ID
- Atualização de dados do usuário
- Exclusão lógica de usuário (soft delete)

### Autenticação
- Login com geração de token JWT
- Validação de credenciais
- Controle de acesso baseado em roles (BASIC, ADMIN)

## Endpoints da API

| Método | Endpoint | Descrição | Autorização |
|--------|----------|-----------|-------------|
| POST | `/users/save` | Cria novo usuário | Público |
| GET | `/users/show-all` | Lista todos usuários | ADMIN |
| GET | `/users/{id}` | Busca usuário por ID | Autenticado |
| PUT | `/users/{id}` | Atualiza usuário | Autenticado |
| DELETE | `/users/{id}` | Remove usuário | Autenticado |
| POST | `/login` | Autentica usuário | Público |

## Documentação

A documentação da API está disponível via Swagger UI:
- **Swagger UI**: `http://localhost:8081/swagger-ui.html`
- **OpenAPI JSON**: `http://localhost:8081/v3/api-docs`

## Pré-requisitos

- Java 21+
- Maven 3.9+
- MySQL 8.0+
- Docker e Docker Compose (opcional)

## Configuração

1. Clone o repositório:
```bash
git clone https://github.com/Ivictors/user-ms.git
cd user-ms
```

2. Configure as variáveis de ambiente:
```bash
cp env-example .env
```

3. Configure o arquivo de propriedades:
```bash
cp src/main/resources/application.example src/main/resources/application.yml
```

4. Configure as chaves JWT RSA:
   - Gere as chaves privada e pública
   - Coloque os arquivos `private-key.pem` e `public-key.pem` no diretório de resources/keys

5. Inicie o banco de dados com Docker:
```bash
docker-compose up -d
```

6. Execute a aplicação:
```bash
./mvnw spring-boot:run
```

## Variáveis de Ambiente

| Variável | Descrição |
|----------|-----------|
| `MYSQL_DATABASE` | Nome do banco de dados |
| `MYSQL_USER` | Usuário do MySQL |
| `MYSQL_PASSWORD` | Senha do MySQL |
| `MYSQL_ROOT_PASSWORD` | Senha root do MySQL |
| `DB_PORT_EXTERNAL` | Porta externa do MySQL |

## Estrutura do Projeto

```
user-ms/
├── src/
│   ├── main/
│   │   ├── java/com/victor/userms/
│   │   │   ├── config/          # Configurações (Security, OpenAPI)
│   │   │   ├── controller/      # Controllers REST
│   │   │   ├── dto/             # Data Transfer Objects
│   │   │   ├── enums/           # Enumerações
│   │   │   ├── exceptions/      # Exceções customizadas
│   │   │   ├── handler/         # Global Exception Handler
│   │   │   ├── model/           # Entidades JPA
│   │   │   ├── repository/      # Repositórios
│   │   │   └── service/         # Lógica de negócio
│   │   └── resources/
│   │       └── application.example
│   └── test/                    # Testes unitários
├── docker-compose.yml
├── env-example
├── pom.xml
└── README.md
```

## Segurança

- Senhas armazenadas com BCrypt
- Autenticação via JWT com RSA
- Controle de acesso por roles
- Validação de entrada de dados
- Soft delete para preservar integridade de dados

## Testes

Execute os testes com:
```bash
./mvnw test
```
