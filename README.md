# Banco API - Spring Boot

## 📌 Descrição
Esta é uma API de gestão bancária desenvolvida com **Spring Boot**. Ela permite criar contas bancárias, realizar transações financeiras (Pix, Crédito e Débito) e consultar o saldo de uma conta.

## 🚀 Tecnologias Utilizadas
- **Java 17**
- **Spring Boot**
- **Spring Web**
- **Spring Data JPA**
- **My SQL Database**
- **Lombok**
- **Spring Validation**
- **JUnit + Mockito** (Testes unitários)

## 📂 Endpoints Disponíveis

### Criar Conta
`POST /conta`
```json
{
  "numero_conta": 234,
  "saldo": 180.37
}
```
**Resposta:**
```json
{
  "numero_conta": 234,
  "saldo": 180.37
}
```

### Consultar Conta
`GET /conta?numero_conta=234`
**Resposta:**
```json
{
  "numero_conta": 234,
  "saldo": 180.37
}
```
Caso a conta não exista, retorna **404 Not Found**.

### Realizar Transação
`POST /transacao`
```json
{
  "forma_pagamento": "D",
  "numero_conta": 234,
  "valor": 10.00
}
```
**Respostas:**
- **201 Created:** Transação aprovada
- **400 Bad Request:** Saldo insuficiente ou conta inexistente

## 📊 Regras de Negócio
- **Pix (P)** → Sem taxa.
- **Débito (D)** → 3% sobre a operação.
- **Crédito (C)** → 5% sobre a operação.
- **Não há cheque especial** (saldo não pode ser negativo).

## 🛠️ Como Executar o Projeto
### 📌 Pré-requisitos
- Ter **Java 17** instalado.
- Ter **Maven** instalado.

### ▶️ Rodando a Aplicação
1. Clone este repositório:
   ```sh
   git clone https://github.com/rodrigohomero/desafio-objective.git
   ```
2. Acesse a pasta do projeto:
   ```sh
   cd banco-api
   ```
3. Execute o projeto com:
   ```sh
   mvn spring-boot:run
   ```

A API estará disponível em: `http://localhost:8080`

## 🧪 Rodando os Testes
Para executar os testes unitários, utilize o comando:
```sh
mvn test
```

## 📝 Considerações Finais
Se precisar de mais informações ou melhorias, entre em contato. 🚀

