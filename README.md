# 🧪 ServeRest API Tests — REST Assured

[![Badge ServeRest](https://img.shields.io/badge/API-ServeRest-green)](https://github.com/ServeRest/ServeRest/)
![Java](https://img.shields.io/badge/Java-17%2B-orange?logo=java)
![Gradle](https://img.shields.io/badge/Build-Gradle-blue?logo=gradle)
![TestNG](https://img.shields.io/badge/TestNG-7.10.2-red)
![REST Assured](https://img.shields.io/badge/REST%20Assured-5.5.6-brightgreen)
![CI](https://img.shields.io/github/actions/workflow/status/geovanegustavo/serverest-api-tests-restassured/ci.yml?label=CI&logo=githubactions)

Projeto de automação de testes de API REST desenvolvido em **Java** com **REST Assured** e **TestNG**, utilizando a API pública [ServeRest](https://serverest.dev) como alvo dos testes.

Este projeto tem como objetivo aplicar e consolidar práticas de automação de testes de API, incluindo validação de contratos via JSON Schema, cobertura de diferentes cenários de resposta (sucesso, autenticação, dados inválidos) e execução contínua via GitHub Actions.

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

O [ServeRest](https://serverest.dev) é uma API REST que simula uma loja virtual e foi criada especificamente para estudos de testes de API. Neste projeto, os testes automatizados cobrem os principais endpoints da API, com foco em:

- Validação de **status codes** de resposta
- Validação do **corpo da resposta** (response body)
- Validação de **contratos via JSON Schema**
- Cenários de **autenticação** (token válido, inválido, ausente)
- Cenários de **dados inválidos** e mensagens de erro esperadas

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
│       └── ci.yml                  # Pipeline de CI com GitHub Actions
├── src/
│   └── test/
│       ├── java/
│       │   ├── tests/              # Classes de teste por endpoint
│       │   └── ...                 # Configurações base, utilitários, POJOs
│       └── resources/
│           ├── testng.xml          # Suite de execução dos testes
│           └── schemas/            # Schemas JSON para validação de contrato
├── build.gradle                    # Configuração do projeto e dependências
├── settings.gradle
├── gradlew / gradlew.bat           # Gradle Wrapper
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

Abra o arquivo no seu navegador para visualizar os resultados detalhados.

---

## Cenários de Teste

Os testes cobrem os seguintes endpoints e cenários do ServeRest:

### `POST /login`
| Cenário | Status Esperado |
|---|---|
| Login com credenciais válidas | 200 OK |
| Login com senha inválida | 400 Bad Request |
| Login sem corpo na requisição | 400 Bad Request |
| Validação do contrato JSON Schema (200) | Schema válido |

> Novos endpoints e cenários serão adicionados progressivamente.

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
