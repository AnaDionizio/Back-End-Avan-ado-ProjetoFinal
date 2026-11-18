# Sistema de Gerenciamento de Encomendas

## Sobre o projeto

O **Sistema de Gerenciamento de Encomendas** é um projeto desenvolvido como parte da disciplina de Back-End. Seu objetivo é 
organizar e controlar o processo de registro e acompanhamento de encomendas, desde o atendimento ao cliente até a entrega do pedido.

O sistema foi pensado para estabelecimentos que recebem encomendas por diferentes canais de atendimento, como WhatsApp, Instagram, 
telefone e presencialmente. Os funcionários são responsáveis por registrar e gerenciar essas solicitações no sistema.

A aplicação permite estruturar informações de clientes, funcionários, produtos, tipos de encomenda, pedidos, 
pagamentos e histórico de alterações.

## Objetivos

- Organizar o cadastro e as informações dos clientes.
- Registrar encomendas e seus respectivos itens.
- Controlar os diferentes status das encomendas.
- Registrar pagamentos realizados pelos clientes.
- Definir prazos para alterações nas encomendas.
- Manter um histórico das alterações realizadas.
- Facilitar o acompanhamento das encomendas pelos funcionários.

## Tecnologias utilizadas

- **Java 21** – linguagem de programação.
- **Spring Boot 4.1.2** – framework para desenvolvimento da aplicação.
- **Maven** – gerenciamento de dependências e construção do projeto.
- **Spring Data JPA** – persistência e mapeamento objeto-relacional.
- **Hibernate** – implementação JPA utilizada pelo projeto.
- **Jakarta Validation** – validação de dados.
- **Microsoft SQL Server** – sistema gerenciador de banco de dados.
- **IntelliJ IDEA** – ambiente de desenvolvimento.

## Arquitetura do projeto

O projeto utiliza a **Arquitetura em Camadas (Layered Architecture)**, que organiza a aplicação 
em partes com responsabilidades específicas.

Essa arquitetura foi escolhida por facilitar a organização do código, a manutenção e a evolução do sistema. 
Também permite separar as regras de negócio do acesso aos dados e da comunicação com o usuário.

### Organização das camadas

- **Entity:** contém as entidades do sistema e seus relacionamentos, representando as
- estruturas que serão persistidas no banco de dados.
- **Repository:** responsável pelo acesso e pela comunicação com o banco de dados.
- **Service:** concentra as regras de negócio e a lógica da aplicação.
- **Controller:** responsável por receber as requisições e encaminhá-las para as camadas responsáveis.
- **DTO:** utilizado para transportar dados entre as partes da aplicação.
- **Mapper:** responsável pela conversão entre entidades e DTOs.

Também existe o pacote `shared`, destinado a elementos compartilhados, como enums e exceções.

## Estrutura de pacotes

A organização do projeto segue a separação por módulos de negócio:

```text
sistema_encomendas/
│
├── cliente/
│   ├── entity/
│   │       └── Cliente
│   ├── repository/
│   ├── service/
│   ├── controller/
│   ├── dto/
│   └── mapper/
│
├── funcionario/
│   ├── entity/
│   │       └── Funcionario
│   ├── repository/
│   ├── service/
│   ├── controller/
│   ├── dto/
│   └── mapper/
│
├── produto/
│   ├── entity/
│   │       └── Produto
│   ├── repository/
│   ├── service/
│   ├── controller/
│   ├── dto/
│   └── mapper/
│
├── tipoencomenda/
│   ├── entity/
│   │       └── TipoEncomenda
│   ├── repository/
│   ├── service/
│   ├── controller/
│   ├── dto/
│   └── mapper/
│
├── encomenda/
│   ├── entity/
│   │       ├── Encomenda
│   │       └── ItemEncomenda
│   ├── repository/
│   ├── service/
│   ├── controller/
│   ├── dto/
│   └── mapper/
│
├── pagamento/
│   ├── entity/
│   │       └── Pagamento
│   ├── repository/
│   ├── service/
│   ├── controller/
│   ├── dto/
│   └── mapper/
│
├── historico/
│   ├── entity/
│   │       └── HistoricoAlteracao
│   ├── repository/
│   ├── service/
│   ├── controller/
│   ├── dto/
│   └── mapper/
│
└── shared/
      ├── enums/
      │       ├── CanalAtendimento
      │       ├── FormaPagamento
      │       ├── SetorFuncionario
      │       └── StatusEncomenda
      └── exception/
```

## Principais entidades

O modelo de domínio é composto pelas seguintes entidades:

| Entidade | Responsabilidade |
|---|---|
| Cliente | Armazena os dados dos clientes. |
| Funcionario | Representa os funcionários que utilizam o sistema. |
| Produto | Armazena os produtos disponíveis para encomenda. |
| TipoEncomenda | Define os tipos de encomenda e seus prazos de alteração. |
| Encomenda | Representa os pedidos registrados no sistema. |
| ItemEncomenda | Relaciona os produtos e as quantidades de cada encomenda. |
| Pagamento | Registra os pagamentos realizados. |
| HistoricoAlteracao | Armazena as alterações realizadas nas encomendas. |

## Relacionamentos entre as entidades

O modelo utiliza relacionamentos JPA para representar as associações entre os elementos do domínio:

- Um **Cliente** pode possuir várias encomendas.
- Um **Funcionario** pode registrar várias encomendas.
- Um **TipoEncomenda** pode estar associado a várias encomendas.
- Uma **Encomenda** possui seus itens, pagamentos e histórico de alterações.
- Um **Produto** pode estar presente em vários itens de encomenda.
- Um **Funcionario** pode registrar vários pagamentos e alterações no histórico.

Os relacionamentos são representados por anotações como `@OneToMany` e `@ManyToOne`.

## Como executar o projeto

### Pré-requisitos

Antes de executar o projeto, é necessário ter instalado:

- Java JDK 21.
- Microsoft SQL Server.
- SQL Server Management Studio (SSMS).
- Git (opcional, para clonar o repositório).
- IntelliJ IDEA ou outra IDE compatível com Java.

### 1. Clonar o repositório

No terminal, execute:

```bash
git clone https://github.com/AnaDionizio/Back-End-Avan-ado-ProjetoFinal.git
```

Entre na pasta do projeto:

```bash
cd sistema-encomendas
```

### 2. Configurar o banco de dados

Crie um banco de dados no SQL Server com o nome:

```sql
CREATE DATABASE sistema_encomendas;
```

### 3. Configurar a conexão

Abra o arquivo:

```text
src/main/resources/application.yaml
```

Configure os dados de conexão com o seu SQL Server, utilizando o endereço, 
a porta, o usuário e a senha correspondentes ao seu ambiente.


### 4. Compilar o projeto

No terminal, dentro da pasta principal do projeto, execute:


```powershell
.\mvnw.cmd clean compile
```

### 5. Executar a aplicação

Para iniciar o projeto, execute:

```powershell
.\mvnw.cmd spring-boot:run
```

## Diagrama UML

O diagrama de classes UML apresenta as entidades do sistema, seus atributos, métodos, relacionamentos e multiplicidades.

O arquivo do diagrama deve estar disponível no repositório, permitindo visualizar a estrutura do modelo de domínio e 
compreender como as entidades se relacionam.

## Status do desenvolvimento

O projeto está em desenvolvimento, com foco na construção do modelo de domínio, definição das entidades, atributos, métodos de negócio, 
relacionamentos JPA e organização arquitetural.

## Autoria

Projeto acadêmico desenvolvido para a disciplina de Back-End.

**Linguagem:** Java  
**Arquitetura:** Layered Architecture (Arquitetura em Camadas)  
**Banco de dados:** Microsoft SQL Server
