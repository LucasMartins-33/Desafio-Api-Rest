# Desafio API REST - Transferências Bancárias

Uma API REST simples, robusta e objetiva em Spring Boot para executar e gerenciar transferências monetárias entre contas, utilizando um banco de dados H2 em memória.
Este projeto foi desenvolvido com foco em **Clean Code**, boas práticas de arquitetura e segurança em transações financeiras.

## Tecnologias Utilizadas

- **Java 21**
- **Spring Boot** (Web, Data JPA, Validation)
- **Banco de Dados H2** (Em memória)
- **Hibernate / JPA**
- **JUnit 5 & Mockito** (Teste Unitários)
- **MockMvc** (Testes de Integração)
-  **Maven**

## Estrutura do Projeto

```markdown
desafio-api-rest/
├── src/main/java/com/example/desafioapirest/
│   ├── controller/  # REST Endpoints (Entrada da API)
│   ├── dto/         # Request/Response Data Transfer Objects
│   ├── entity/      # Entidades JPA (Tabelas do Banco)
│   ├── exception/   # Tratamento Global de Exceções
│   ├── repository/  # Interfaces do Spring Data JPA
│   └── service/     # Regras de Negócio e Validações
├── src/main/resources/
│   └── application.properties # Configurações da aplicação e Banco H2
└── src/test/java/com/example/desafioapirest/
    ├── controller/  # Testes de Integração (MockMvc + SpringBootTest)
    └── service/     # Testes Unitários (JUnit + Mockito)
```

## Pré-requisitos

- **Java21 ou superior**
- **Maven 3.6+**

## Instalação
Série de passos para ter o ambiente de desenvolvimento em execução na sua máquina.

1. Clone o repositório:
```markdown
git clone https://github.com/LucasMartins-33/Desafio-Api-Rest.git
```
2. Acesse a pasta do projeto via terminal e execute a aplicação:
```markdown
cd desafio-api-rest
./mvnw spring-boot:run
```

A API estará imediatamente disponível em `http://localhost:8080`.

Acessando o Banco de Dados (H2 Console) para visualizar as tabelas sendo criadas e alteradas em tempo real:
- **URL** `http://localhost:8080/h2-console`
- **JDBC URL** `jdbc:h2:mem:transferdb`
- **Usuário** `sa`
- **Senha** `test123` (Caso queira tirar a senha ou mudar, entre em application.properties em spring.datasource.password)




 ## Funcionalidades e Diferênciais

 - [x] **Criação de Contas.** Endpoints para criar contas com saldo inicial.
 - [X] **Transferências Seguras.** Transferência de valores validando regras de negócio (ex: saldo insuficiente, conta não encontrada).
 - [x] **Proteção contra Concorrência e Condições de Corrida:** Implementação de *Pessimistic Locking* (`@Lock(LockModeType.PESSIMISTIC_WRITE)`) no banco de dados para garantir a consistência do saldo em requisições simultâneas.
 - [X] **Cobertura de Testes:** Testes unitários isolando regras de negócio e testes de integração validando desde o endpoint até o banco de dados.

## Executando os testes
Psra executar a suíte completa de testes automatizados, rode o comando

```markdown
./mvnw test
```

### Analise os testes de ponta a ponta (integração)
Estes testes verificam o fluxo completo da aplicação. Utilizando @SpringBootTest e MockMvc, eles disparam requisições HTTP simuladas 
e verificam se o banco de dados H2 real foi atualizado com a matemática correta (ex: subtração na conta origem e adição no destino).

### Testes de estilo de codificação (Unitários)
Estes testes isolam as regras de negócio puras (camada Service). Utilizando JUnit 5 e Mockito, verificamos o "caminho feliz" e o 
"caminho triste" (exceções) simulando as respostas do banco de dados, garantindo que a lógica funcione independente da infraestrutura.

## Endpoints da API

1. Criar uma Conta
**POST** `http://localhost:8080/api/accounts`

- **Request:**

```markdown
{
  "initialBalance": 1000.00
}
```

- **Response (201 Created)**
```markdown
{
  "id": 1,
  "balance": 1000.00
}
```


2. Buscar Conta por ID
**GET** `http://localhost:8080/api/accounts/{id}`

- **Response (200 OK):**
```markdown
{
  "id": 1,
  "balance": 1000.00
}
```
(Retorna 400 Bad Request e uma mensagem: Conta não encontrada)
   
3. Realizar Transferência
**POST** `http://localhost:8080/api/transfers`

- **Request**
```markdown
{
  "sourceAccountId": 1,
  "targetAccountId": 2,
  "amount": 150.00
}
```
- **Response (200 OK):** Transferência realizada com sucesso!

## Respostas de Erro

Erros gerados por validações ou regras de negócio seguem um padrão de resposta amigável

Status HTTP Causa Comum
400 Bad Request `Saldo insuficiente na conta de origem!`, `O valor da transferência deve ser maior que zero!` (violação do @PositivoOrZero), IDs iguais.
404 Not Found Conta de origem ou destino inexistente.

## Teste Rápido (cURL)
Caso queira testar o sistema rapidamente pelo terminal:

```markdown
{
  # 1. Cria a conta de origem com saldo de R$ 1000.00
curl -X POST http://localhost:8080/api/accounts -H "Content-Type: application/json" -d "{\"initialBalance\": 1000.00}"

# 2. Cria a conta de destino com saldo de R$ 200.00
curl -X POST http://localhost:8080/api/accounts -H "Content-Type: application/json" -d "{\"initialBalance\": 200.00}"

# 3. Executa uma transferência de R$ 150.00 da conta 1 para a 2
curl -X POST http://localhost:8080/api/transfers -H "Content-Type: application/json" -d "{\"sourceAccountId\": 1, \"targetAccountId\": 2, \"amount\": 150.00}"

# 4. Verifica os saldos atualizados
curl http://localhost:8080/api/accounts/1
curl http://localhost:8080/api/accounts/2
}
```

## Decisões Técnicas

- **Precisão dos dados:**  Foi utilizado `BigDecimal` em vez de `Double` para lidar com todos os valores financeiros, a fim de evitar erros crônicos de arredondamento de ponto flutuante no Java.
- **Segurança Concorrente:** Utilizado o `LockModeType.PESSIMISTIC_WRITE` (que traduz para `SELECT ... FOR UPDATE` no SQL) para criar uma fila de espera no banco de dados,
protegendo o sistema contra inconsistências caso dois usuários façam transferências no exato mesmo milissegundo.
- **Separação de Responsabilidade:** O contrato da API foi blindado usando `DTOs` (Data Transfer Objects), garantindo que as requisições web (JSON) não tenham acesso direto à entidade do banco de dados (JPA Entity).




