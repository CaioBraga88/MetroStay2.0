# Projeto MetroStay - Backend (trabalho-api)

## Descrição

Este projeto implementa uma API RESTful de Gerenciamento de Reservas (MetroStay), desenvolvida em Spring Boot. O objetivo principal é garantir a integridade dos dados através da aplicação de regras de negócio estritas para validação de datas e verificação de disponibilidade de quartos, seguindo uma arquitetura limpa em camadas (Controller, Service, Repository).

## Regras de Negócio Implementadas

Validação de Datas: Data de check-out deve ser posterior à data de check-in. O check-in não pode ser agendado para datas passadas.

Disponibilidade: Verificação de conflito de datas para garantir que o quarto não esteja ocupado no período desejado (durante a criação ou atualização da reserva).

Hóspede Válido: Simulação de validação para garantir que um hospedeId válido está associado à reserva.

## Arquitetura e Camadas

O projeto utiliza o padrão de arquitetura em três camadas:

Controller (inbound): Recebe requisições HTTP, usa DTOs e roteia chamadas para o Service. Possui Javadoc para documentação Swagger.

Service (core/service): Contém toda a lógica de negócio e validação.

Repository (core/repository): Acessa a camada de persistência.

## Tecnologias Utilizadas

Linguagem: Java 17+

Framework: Spring Boot 3

Persistência: Spring Data JPA

Banco de Dados: H2 Database (em memória, para testes e desenvolvimento rápido)

Build: Maven

## Pré-requisitos

JDK 17 ou superior

Maven 3.x

Instalação

## Clone o repositório:

git clone (https://github.com/CaioBraga88/MetroStay2.0.git)

## Navegue até o diretório do projeto:

cd MetroStay\trabalho-api-main

## Configure o banco de dados:

Para o ambiente de desenvolvimento, o H2 (em memória) é usado por padrão.

Para um ambiente de produção/persistente, edite o arquivo application.properties (ou application.yaml) com as configurações do seu banco de dados.

## Compile e execute o projeto:

mvn clean install
mvn spring-boot:run

## A API estará disponível em http://localhost:8080.

Documentação da API (Swagger)

A documentação da API pode ser acessada por meio do Swagger. Após iniciar o backend, você pode acessar a documentação por meio da seguinte URL:

http://localhost:8080/swagger-ui.html

## Endpoints

Abaixo está a descrição dos principais endpoints da API para o recurso Reserva (/api/v1/reservas):

1. GET /api/v1/reservas

Descrição: Retorna a lista de todas as reservas registradas.

Resposta:

200 OK

[
  {
    "id": 1,
    "numeroDoQuarto": "101A",
    "dataInicioReserva": "2025-12-01",
    "dataFinalReserva": "2025-12-05",
    "hospedeId": 42
  }
]



2. POST /api/v1/reservas

Descrição: Cria uma nova reserva, aplicando todas as regras de negócio e validações de disponibilidade.

Corpo da Requisição (ReservaDto):

{
  "numeroDoQuarto": "205",
  "dataInicioReserva": "2026-01-10",
  "dataFinalReserva": "2026-01-15",
  "hospedeId": 12
}



Resposta:

201 Created (Reserva criada com sucesso)

400 Bad Request (Erro de validação, ex: conflito de datas ou quarto já ocupado)

3. GET /api/v1/reservas/{id}

Descrição: Retorna uma reserva específica pelo seu ID.

Parâmetros de Caminho:

id: ID da reserva.

Resposta:

200 OK (Reserva encontrada)

404 Not Found (Se a reserva não for encontrada)

4. PUT /api/v1/reservas/{id}

Descrição: Atualiza os dados de uma reserva existente, re-executando todas as regras de validação.

Corpo da Requisição (ReservaDto):

{
  "numeroDoQuarto": "205",
  "dataInicioReserva": "2026-01-12",
  "dataFinalReserva": "2026-01-17",
  "hospedeId": 12
}



Parâmetros de Caminho:

id: ID da reserva a ser atualizada.

Resposta:

200 OK (Reserva atualizada)

404 Not Found (Se a reserva não for encontrada)

400 Bad Request (Erro de validação, ex: novo período entra em conflito)

5. DELETE /api/v1/reservas/{id}

Descrição: Remove uma reserva pelo ID.

Parâmetros de Caminho:

id: ID da reserva.

Resposta:

204 No Content (Deletado com sucesso)

404 Not Found (Se a reserva não for encontrada)

Desenvolvido como parte do trabalho acadêmico de 

$$ Caio, Lucas, Felipe e Victor / MetroStay $$

.