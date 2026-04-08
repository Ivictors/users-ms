# Users App - Sistema de Gestão de Usuários

Aplicação monolítica para gestão de usuários, desenvolvida com Spring Boot, oferecendo operações CRUD completas, autenticação segura via JWT com criptografia RSA, e controle de acesso baseado em roles.

## 🚀 Tecnologias

- **Java 21**
- **Spring Boot 3.4.2**
- **Spring Security** - Autenticação e autorização com OAuth2 Resource Server
- **Spring Data JPA** - Persistência de dados
- **MySQL 8.0** - Banco de dados relacional
- **SpringDoc OpenAPI** - Documentação automática da API
- **Bean Validation** - Validação de dados
- **BCrypt** - Criptografia de senhas

## 📐 Arquitetura

```
┌─────────────────────────────────────────────────────┐
│                    Users App                         │
├─────────────────────────────────────────────────────┤
│              Controllers (REST API)                  │
│  ├── UserController - CRUD de usuários              │
│  └── LoginController - Autenticação                 │
├─────────────────────────────────────────────────────┤
│                    Services                          │
│  ├── UserService - Lógica de negócio de usuários    │
│  └── LoginService - Lógica de autenticação          │
├─────────────────────────────────────────────────────┤
│            Repository (Spring Data JPA)              │
│  ├── UserRepository                                  │
│  └── RoleRepository                                  │
├─────────────────────────────────────────────────────┤
│                 Models & DTOs                        │
│  ├── UserModel, Role                                │
│  └── UserRequestDto, UserResponseDto, LoginDto      │
└─────────────────────────────────────────────────────┘
```

## ⚙️ Funcionalidades

### Gestão de Usuários
- ✅ Cadastro de novos usuários
- ✅ Listagem de usuários (admin)
- ✅ Busca de usuário por ID
- ✅ Atualização de dados do usuário
- ✅ Exclusão lógica de usuário (soft delete)

### Autenticação e Segurança
- ✅ Login com geração de token JWT
- ✅ Validação de credenciais
- ✅ Controle de acesso baseado em roles (BASIC, ADMIN)
- ✅ Criptografia RSA para tokens JWT
- ✅ Senhas armazenadas com BCrypt

## 📡 Endpoints da API

| Método | Endpoint          | Descrição                    | Autorização |
|--------|-------------------|------------------------------|-------------|
| POST   | `/users/save`     | Cria novo usuário            | Público     |
| GET    | `/users/show-all` | Lista todos usuários         | ADMIN       |
| GET    | `/users/{id}`     | Busca usuário por ID         | Autenticado |
| PUT    | `/users/{id}`     | Atualiza usuário             | Autenticado |
| DELETE | `/users/{id}`     | Remove usuário (soft delete) | Autenticado |
| POST   | `/login`          | Autentica usuário            | Público     |

## 📖 Documentação

A documentação da API está disponível via Swagger UI:
- **Swagger UI**: `http://localhost:8081/swagger-ui.html`
- **OpenAPI JSON**: `http://localhost:8081/v3/api-docs`

## 🛠️ Pré-requisitos

- Java 21+
- Maven 3.9+
- MySQL 8.0+
- Docker e Docker Compose (opcional)

## ⚙️ Configuração

### 1. Clone o repositório
```bash
git clone https://github.com/Ivictor/users-app.git
cd users-app
```

### 2. Configure as variáveis de ambiente
```bash
cp env-example .env
```

### 3. Configure o arquivo de propriedades
```bash
cp src/main/resources/application.example src/main/resources/application-dev.yml
```

### 4. Configure as chaves JWT RSA
Gere as chaves privada e pública RSA e coloque os arquivos `private_key.pem` e `public_key.pem` em `src/main/resources/`.

### 5. Inicie o banco de dados com Docker
```bash
docker-compose up -d
```

### 6. Execute a aplicação
```bash
./mvnw spring-boot:run
```

A aplicação estará disponível em: `http://localhost:8081`

## 🔐 Variáveis de Ambiente

| Variável              | Descrição                    |
|-----------------------|------------------------------|
| `MYSQL_DATABASE`      | Nome do banco de dados       |
| `MYSQL_USER`          | Usuário do MySQL             |
| `MYSQL_PASSWORD`      | Senha do MySQL               |
| `MYSQL_ROOT_PASSWORD` | Senha root do MySQL          |
| `DB_PORT_EXTERNAL`    | Porta externa do MySQL       |

## 📁 Estrutura do Projeto

```
users-app/
├── src/
│   ├── main/
│   │   ├── java/com/victor/usersapp/
│   │   │   ├── config/        # Configurações (Security, OpenAPI)
│   │   │   ├── controller/    # Controllers REST
│   │   │   ├── dto/           # Data Transfer Objects
│   │   │   ├── enums/         # Enumerações
│   │   │   ├── exceptions/    # Exceções customizadas
│   │   │   ├── handler/       # Global Exception Handler
│   │   │   ├── model/         # Entidades JPA
│   │   │   ├── repository/    # Repositórios
│   │   │   └── service/       # Lógica de negócio
│   │   └── resources/
│   │       ├── application-dev.yml
│   │       ├── private_key.pem
│   │       └── public_key.pem
│   └── test/                  # Testes unitários
├── docker-compose.yml
├── env-example
├── pom.xml
└── README.md
```

## 🔒 Segurança

- **Senhas**: Armazenadas com BCrypt (hash unidirecional)
- **Autenticação**: JWT com criptografia RSA (chave pública/privada)
- **Autorização**: Controle de acesso baseado em roles (BASIC, ADMIN)
- **Validação**: Validação de entrada de dados com Bean Validation
- **Soft Delete**: Exclusão lógica para preservar integridade de dados

## 🧪 Testes

Execute os testes unitários com:
```bash
./mvnw test
```

Para executar com relatório de cobertura:
```bash
./mvnw test jacoco:report
```

## 📝 Licença

Este projeto está sob a licença MIT.

## 👤 Autor

Victor - [GitHub](https://github.com/Ivictors)
