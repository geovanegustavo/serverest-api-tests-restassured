# 🧪 ServeRest API Tests — REST Assured

[![Badge ServeRest](https://img.shields.io/badge/API-ServeRest-green)](https://github.com/ServeRest/ServeRest/)
![Java](https://img.shields.io/badge/Java-17%2B-orange?logo=java)
![Gradle](https://img.shields.io/badge/Build-Gradle-blue?logo=gradle)
![TestNG](https://img.shields.io/badge/TestNG-7.10.2-red)
![REST Assured](https://img.shields.io/badge/REST%20Assured-5.5.6-brightgreen)
![CI](https://img.shields.io/github/actions/workflow/status/geovanegustavo/serverest-api-tests-restassured/ci.yml?label=CI&logo=githubactions)

Projeto de automação de testes de API REST desenvolvido em **Java** com **REST Assured** e **TestNG**, utilizando a API pública [ServeRest](https://serverest.dev) como alvo dos testes.

Este projeto tem como objetivo aplicar e consolidar práticas de automação de testes de API, incluindo validação de contratos via JSON Schema, cobertura de fluxos completos de CRUD e execução contínua via GitHub Actions.

---

## 📋 Índice

- [Sobre o Projeto](#sobre-o-projeto)
- [Tecnologias e Dependências](#tecnologias-e-dependências)
- [Estrutura do Projeto](#estrutura-do-projeto)
- [Pré-requisitos](#pré-requisitos)
- [Como Executar](#como-executar)
- [Cenários de Teste](#cenários-de-teste)
- [CI/CD com GitHub Actions](#cicd-com-github-actions)
- [Autor](#autor)

---

## Sobre o Projeto

O [ServeRest](https://serverest.dev) é uma API REST que simula uma loja virtual e foi criada especificamente para estudos de testes de API. Neste projeto, os testes automatizados cobrem os principais endpoints da API com foco em:

- Validação de **status codes** de resposta
- Validação do **corpo da resposta** (response body)
- Validação de **contratos via JSON Schema**
- Fluxos completos de **CRUD** para usuários e produtos
- Cenários de **autenticação** com token JWT
- Cenários de **exceção** (produto duplicado, token ausente, ID inexistente)

---

## Tecnologias e Dependências

| Tecnologia | Versão | Finalidade |
|---|---|---|
| Java | 17+ | Linguagem principal |
| Gradle | Wrapper incluso | Gerenciador de build |
| TestNG | 7.10.2 | Framework de testes |
| REST Assured | 5.5.6 | Biblioteca de testes de API |
| JSON Schema Validator | 5.4.0 | Validação de contratos de resposta |
| Jackson Databind | 2.17.2 | Serialização/desserialização de JSON |
| SLF4J | 2.0.12 | Logging |

---

## Estrutura do Projeto

```
serverest-api-tests-restassured/
├── .github/
│   └── workflows/
│       └── ci.yml                        # Pipeline de CI com GitHub Actions
├── src/
│   └── test/
│       ├── java/
│       │   └── serverest/
│       │       ├── tests/
│       │       │   ├── LoginTest.java    # Testes do endpoint POST /login
│       │       │   ├── UsuarioTest.java  # Testes dos endpoints de /usuarios
│       │       │   └── ProdutoTest.java  # Testes dos endpoints de /produtos
│       │       ├── model/                # POJOs (Usuario, Produto)
│       │       └── util/                 # Helpers, TokenHolder, Mensagens
│       └── resources/
│           ├── testng.xml                # Suite de execução dos testes
│           └── schemas/                  # JSON Schemas para validação de contrato
│               ├── login/
│               ├── usuario/
│               └── produto/
├── build.gradle
├── settings.gradle
├── gradlew / gradlew.bat
└── README.md
```

---

## Pré-requisitos

- **Java 17** ou superior instalado
- **Git** instalado
- Conexão com a internet (os testes apontam para `https://serverest.dev`)

> Não é necessário instalar o Gradle separadamente — o projeto usa o **Gradle Wrapper** (`gradlew`).

---

## Como Executar

### 1. Clone o repositório

```bash
git clone https://github.com/geovanegustavo/serverest-api-tests-restassured.git
cd serverest-api-tests-restassured
```

### 2. Execute os testes

**Linux / macOS:**
```bash
./gradlew test
```

**Windows:**
```bash
gradlew.bat test
```

### 3. Visualize o relatório

Após a execução, o relatório do TestNG estará disponível em:

```
build/reports/tests/test/index.html
```

---

## Cenários de Teste

### 👤 `UsuarioTest` — Endpoints `/usuarios`

| Método | Endpoint | Cenário | Status Esperado |
|---|---|---|---|
| POST | `/usuarios` | Deve cadastrar um usuário administrador com credenciais válidas | 201 Created |
| POST | `/usuarios` | Deve cadastrar um usuário comum com credenciais válidas | 201 Created |
| GET | `/usuarios/{id}` | Deve listar o usuário cadastrado pelo id | 200 OK |
| GET | `/usuarios` | Deve pesquisar usuário cadastrado pelo nome | 200 OK |
| PUT | `/usuarios/{id}` | Deve editar o usuário já cadastrado | 200 OK |
| PUT | `/usuarios/{id}` | Deve cadastrar o usuário quando o id informado não existe | 201 Created |
| DELETE | `/usuarios/{id}` | Deve excluir o usuário cadastrado pelo id | 200 OK |

---

### 🔐 `LoginTest` — Endpoint `/login`

| Método | Endpoint | Cenário | Status Esperado |
|---|---|---|---|
| POST | `/login` | Deve logar usuário administrador com credenciais válidas e retornar token JWT | 200 OK |

> O token retornado é armazenado via `TokenHolder` e reutilizado nos testes de `/produtos`.

---

### 📦 `ProdutoTest` — Endpoints `/produtos`

| Método | Endpoint | Cenário | Status Esperado |
|---|---|---|---|
| POST | `/produtos` | Deve cadastrar um produto aleatório na base de dados | 201 Created |
| GET | `/produtos/{id}` | Deve listar o produto cadastrado pelo id | 200 OK |
| GET | `/produtos` | Deve pesquisar produto cadastrado pelo nome | 200 OK |
| PUT | `/produtos/{id}` | Deve editar o produto já cadastrado | 200 OK |
| DELETE | `/produtos/{id}` | Deve excluir o produto cadastrado pelo id | 200 OK |
| GET | `/produtos/{id}` | Não deve encontrar produto já excluído | 400 Bad Request |
| POST | `/produtos` | NÃO deve cadastrar um produto já existente na base de dados | 400 Bad Request |
| POST | `/produtos` | NÃO deve cadastrar um produto sem token de autenticação | 401 Unauthorized |

---

> Todos os cenários incluem validação de **contrato JSON Schema** e verificação das **mensagens de retorno** da API.

---

## CI/CD com GitHub Actions

O projeto possui um pipeline configurado com **GitHub Actions** que executa automaticamente toda a suíte de testes a cada `push` ou `pull request` na branch `master`.

O arquivo de configuração do workflow está em:
```
.github/workflows/ci.yml
```

---

## Autor

**Geovane Gustavo**
- GitHub: [@geovanegustavo](https://github.com/geovanegustavo)

---

> Projeto desenvolvido como parte de estudos práticos em automação de testes de API com Java e REST Assured.
