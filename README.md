# Safety - Sistema de Agendamento Médico

[![Java](https://img.shields.io/badge/Java-17-orange.svg)](https://openjdk.java.net/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-4.0.5-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![PostgreSQL](https://img.shields.io/badge/PostgreSQL-14+-blue.svg)](https://www.postgresql.org/)

Sistema de gerenciamento de agendamentos médicos desenvolvido com Spring Boot, oferecendo uma API RESTful para criação, atualização, cancelamento e acompanhamento de agendamentos.

## Índice

- [Funcionalidades](#funcionalidades)
- [Tecnologias Utilizadas](#tecnologias-utilizadas)
- [Pré-requisitos](#pré-requisitos)
- [Instalação e Configuração](#instalação-e-configuração)
- [Executando a Aplicação](#executando-a-aplicação)
- [Documentação da API](#documentação-da-api)
- [Endpoints](#endpoints)
- [Modelo de Dados](#modelo-de-dados)
- [Status dos Agendamentos](#status-dos-agendamentos)
- [Validações](#validações)
- [Contribuição](#contribuição)
- [Licença](#licença)

## Funcionalidades

- **Criar Agendamento**: Cadastro de novos agendamentos com validação de intervalo de datas e verificação de conflitos
- **Editar Agendamento**: Atualização de dados de agendamentos existentes
- **Cancelar Agendamento**: Cancelamento de agendamentos com alteração de status
- **Concluir Agendamento**: Marcação de agendamentos como concluídos
- **Buscar por ID**: Consulta de agendamentos individuais
- **Listar Todos**: Listagem completa de todos os agendamentos
- **Detecção de Conflitos**: Validação automática de horários sobrepostos para o mesmo cliente

## Tecnologias Utilizadas

| Tecnologia | Versão | Descrição |
|------------|--------|-----------|
| Java | 17 | Linguagem de programação |
| Spring Boot | 4.0.5 | Framework principal |
| Spring Data JPA | - | Persistência de dados |
| Spring Web MVC | - | Camada web/REST |
| Spring Validation | - | Validação de dados |
| Flyway | - | Migrações de banco de dados |
| PostgreSQL | 14+ | Banco de dados relacional |
| Lombok | - | Redução de boilerplate |
| Springdoc OpenAPI | 2.8.9 | Documentação Swagger |

## Pré-requisitos

Antes de começar, certifique-se de ter instalado:

- [JDK 17](https://adoptium.net/) ou superior
- [Maven 3.6+](https://maven.apache.org/)
- [PostgreSQL 14+](https://www.postgresql.org/download/)
- [Git](https://git-scm.com/)

## Instalação e Configuração

### 1. Clone o repositório

```bash
git clone https://github.com/seu-usuario/safety.git
cd safety
```

### 2. Configure o banco de dados

Crie um banco de dados PostgreSQL chamado `SafeDb`:

```sql
CREATE DATABASE "SafeDb";
```

### 3. Configure as credenciais do banco

Edite o arquivo `src/main/resources/application.properties` com suas credenciais:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/SafeDb
spring.datasource.username=postgres
spring.datasource.password=sua-senha
```

### 4. Instale as dependências

```bash
mvn clean install
```

### 5. Execute as migrações

As migrações do Flyway serão executadas automaticamente ao iniciar a aplicação.

## Executando a Aplicação

### Modo desenvolvimento

```bash
mvn spring-boot:run
```

### Modo produção (build)

```bash
mvn clean package
java -jar target/Safety-0.0.1-SNAPSHOT.jar
```

A aplicação estará disponível em: `http://localhost:8080`

## Documentação da API

Com a aplicação rodando, acesse a documentação Swagger interativa:

| Descrição | URL |
|-----------|-----|
| **Swagger UI** | http://localhost:8080/swagger-ui.html |
| **OpenAPI JSON** | http://localhost:8080/api-docs |

A Swagger UI oferece:
- Visualização interativa de todos os endpoints
- Teste direto das operações via interface web
- Modelos de requisição/resposta detalhados
- Esquemas de dados completos

## Endpoints

Todos os endpoints estão prefixados com `/api/Agendamentos`

| Método | Endpoint | Descrição |
|--------|----------|-----------|
| `POST` | `/criar` | Cria um novo agendamento |
| `POST` | `/{id}/editar` | Atualiza um agendamento existente |
| `POST` | `/{id}/excluir` | Cancela um agendamento |
| `PUT` | `/{id}/concluir` | Marca um agendamento como concluído |
| `GET` | `/{id}` | Busca um agendamento por ID |
| `GET` | `/ativos` | Lista todos os agendamentos |

### Exemplos de Requisições

#### Criar Agendamento

```bash
curl -X POST http://localhost:8080/api/Agendamentos/criar \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Consulta de Rotina",
    "description": "Check-up anual",
    "startDate": "2026-05-01T10:00:00",
    "endDate": "2026-05-01T11:00:00",
    "client": "João Silva"
  }'
```

#### Buscar por ID

```bash
curl http://localhost:8080/api/Agendamentos/1
```

#### Listar Todos

```bash
curl http://localhost:8080/api/Agendamentos/ativos
```

#### Editar Agendamento

```bash
curl -X POST http://localhost:8080/api/Agendamentos/1/editar \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Consulta de Retorno",
    "startDate": "2026-05-15T14:00:00",
    "endDate": "2026-05-15T15:00:00",
    "client": "João Silva"
  }'
```

#### Concluir Agendamento

```bash
curl -X PUT http://localhost:8080/api/Agendamentos/1/concluir
```

#### Cancelar Agendamento

```bash
curl -X POST http://localhost:8080/api/Agendamentos/1/excluir
```

## Modelo de Dados

### Scheduling (Agendamento)

| Campo | Tipo | Descrição |
|-------|------|-----------|
| `id` | Long | ID único do agendamento (auto-incremento) |
| `name` | String (120) | Nome/título do agendamento |
| `description` | Text | Descrição detalhada |
| `startDate` | LocalDateTime | Data e hora de início |
| `endDate` | LocalDateTime | Data e hora de término |
| `client` | String (80) | Nome ou identificador do cliente |
| `status` | Enum | Status do agendamento |
| `createdAt` | LocalDateTime | Data de criação (automático) |
| `attAt` | LocalDateTime | Data da última atualização |

## Status dos Agendamentos

| Status | Descrição |
|--------|-----------|
| `SCHEDULED` | Agendamento programado |
| `CANCELED` | Agendamento cancelado |
| `COMPLETED` | Agendamento concluído |

## Validações

O sistema aplica as seguintes validações:

| Validação | Descrição |
|-----------|-----------|
| **Nome obrigatório** | Campo `name` não pode ser vazio |
| **Datas obrigatórias** | `startDate` e `endDate` são obrigatórios |
| **Data futura** | Datas devem ser posteriores à data atual |
| **Intervalo válido** | `startDate` deve ser anterior a `endDate` |
| **Conflito de horário** | Não permite agendamentos sobrepostos para o mesmo cliente |
| **Tamanho máximo** | `name`: 120 caracteres, `client`: 80 caracteres |

## Estrutura do Projeto

```
Safety/
├── src/
│   ├── main/
│   │   ├── java/com/limasoftware/Safety/
│   │   │   ├── config/          # Configurações (OpenAPI/Swagger)
│   │   │   ├── controller/      # Controladores REST
│   │   │   ├── dto/             # Data Transfer Objects
│   │   │   ├── enums/           # Enumerações (Status, UserRole, SchedulingType)
│   │   │   ├── mapper/          # Mapeamento entre entidades e DTOs
│   │   │   ├── model/           # Entidades JPA
│   │   │   ├── repository/      # Repositórios JPA
│   │   │   ├── service/         # Regras de negócio
│   │   │   └── SafetyApplication.java
│   │   └── resources/
│   │       ├── application.properties
│   │       └── db/migration/    # Scripts Flyway
│   └── test/
│       └── java/com/limasoftware/Safety/
│           ├── controller/      # Testes de controller
│           ├── service/         # Testes de service
│           └── mapper/          # Testes de mapper
├── pom.xml
├── README.md
├── run-tests.sh     # Script para rodar testes (Linux/Mac)
└── run-tests.bat    # Script para rodar testes (Windows)
```

## Testes Unitários

O projeto possui testes unitários cobrindo as principais funcionalidades:

### Classes Testadas

| Classe | Testes | Cobertura |
|--------|--------|-----------|
| `SchedulingService` | 14 testes | Create, Update, Delete, Finish, Search, List |
| `SchedulingController` | 12 testes | Todos os endpoints da API |
| `SchedulingMapper` | 6 testes | Conversões e merge de dados |

### Executando os Testes

**Windows:**
```bash
run-tests.bat
# ou
mvn clean test
```

**Linux/Mac:**
```bash
./run-tests.sh
# ou
mvn clean test
```

### Estrutura dos Testes

- **SchedulingServiceTest**: Testa regras de negócio, validações e detecção de conflitos
- **SchedulingControllerTest**: Testa endpoints HTTP com MockMvc
- **SchedulingMapperTest**: Testa conversões entre DTO e Entity

### Frameworks Utilizados

- JUnit 5 (Jupiter)
- Mockito (mocks e spies)
- Spring Boot Test (@WebMvcTest, @SpringBootTest)
- AssertJ (asserções fluentes)

## Licença

Este projeto está sob a licença MIT. Consulte o arquivo [LICENSE](LICENSE) para detalhes.

---

**Desenvolvido por Guilherme Lima**
