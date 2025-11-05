# Projeto {Nome do Projeto} - Backend (trabalho-api)

## Descrição

{Descrição do projeto com o objetivo principal}

## Tecnologias Utilizadas

- Java {versão}
- {outras tecnologias utilizadas}

## Pré-requisitos

- JDK {versão}
- {outros pré-requisitos}

## Instalação

1. **Clone o repositório:**

   ```bash
   git clone https://link-do-repo.git
   ```

2. **Navegue até o diretório do projeto:**

   ```bash
   cd diretorio-do-projeto
   ```

3. **Configure o banco de dados:**

   Edite o arquivo [application.yaml](src/main/resources/application.yaml) com as configurações do seu banco de dados.

4. **Compile e execute o projeto:**

   ```bash
   mvn clean install
   mvn spring-boot:run
   ```

   A API estará disponível em `http://localhost:8080`.

## Documentação da API (Swagger)

A documentação da API pode ser acessada por meio do Swagger. Após iniciar o backend, você pode acessar a documentação por meio da seguinte URL:

[/swagger-ui/index.html](http://localhost:8080//swagger-ui/index.html)

## Endpoints

Abaixo está a descrição dos principais endpoints da API:

{Alterar os endpoints conforme os endpoints do projeto}

### **1. GET /api/usuarios**

- **Descrição:** Retorna uma lista de usuários.
- **Parâmetros de Consulta:**
  - `page` (opcional): Número da página.
  - `size` (opcional): Número de itens por página.
- **Resposta:**
  - **200 OK**
    ```json
    [
      {
        "id": 1,
        "nome": "João",
        "email": "joao@exemplo.com"
      },
      // ...
    ]
    ```

### **2. POST /api/usuarios**

- **Descrição:** Cria um novo usuário.
- **Corpo da Requisição:**
  ```json
  {
    "nome": "Maria",
    "email": "maria@exemplo.com"
  }
  ```
- **Resposta:**
  - **201 Created**
    ```json
    {
      "id": 2,
      "nome": "Maria",
      "email": "maria@exemplo.com"
    }
    ```

### **3. GET /api/usuarios/{id}**

- **Descrição:** Retorna um usuário específico pelo ID.
- **Parâmetros de Caminho:**
  - `id`: ID do usuário.
- **Resposta:**
  - **200 OK**
    ```json
    {
      "id": 1,
      "nome": "João",
      "email": "joao@exemplo.com"
    }
    ```
  - **404 Not Found** (se o usuário não for encontrado)

### **4. PUT /api/usuarios/{id}**

- **Descrição:** Atualiza um usuário existente.
- **Corpo da Requisição:**
  ```json
  {
    "nome": "João Atualizado",
    "email": "joaoatualizado@exemplo.com"
  }
  ```
- **Parâmetros de Caminho:**
  - `id`: ID do usuário.
- **Resposta:**
  - **200 OK**
    ```json
    {
      "id": 1,
      "nome": "João Atualizado",
      "email": "joaoatualizado@exemplo.com"
    }
    ```
  - **404 Not Found** (se o usuário não for encontrado)

### **5. DELETE /api/usuarios/{id}**

- **Descrição:** Remove um usuário pelo ID.
- **Parâmetros de Caminho:**
  - `id`: ID do usuário.
- **Resposta:**
  - **204 No Content**
  - **404 Not Found** (se o usuário não for encontrado)
