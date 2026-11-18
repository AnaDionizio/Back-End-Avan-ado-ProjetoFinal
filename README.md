# Sistema de Gerenciamento de Encomendas

Projeto acadêmico da disciplina de **Back-End Avançado**. Esta etapa (2ª entrega) concentra-se na **modelagem do domínio** — entidades, atributos, relacionamentos JPA e métodos de negócio — e na **definição da arquitetura** do sistema. Controllers, Services e endpoints serão implementados nas próximas etapas.

---

## Sumário

1. [Sobre o projeto](#sobre-o-projeto)
2. [Objetivos](#objetivos)
3. [Tecnologias utilizadas](#tecnologias-utilizadas)
4. [Modelo de domínio](#modelo-de-domínio)
5. [Diagrama de classes UML](#diagrama-de-classes-uml)
6. [Arquitetura do projeto](#arquitetura-do-projeto)
7. [Organização do código](#organização-do-código)
8. [Como executar o projeto](#como-executar-o-projeto)
9. [Status do desenvolvimento](#status-do-desenvolvimento)
10. [Integrantes](#integrantes)

---

## Sobre o projeto

O **Sistema de Gerenciamento de Encomendas** organiza e controla o processo de registro e acompanhamento de encomendas, desde o atendimento ao cliente até a entrega do pedido.

O sistema é voltado para estabelecimentos que recebem encomendas por diferentes canais de atendimento — **WhatsApp, Instagram, telefone e presencialmente**. Os funcionários registram e gerenciam essas solicitações, acompanham o andamento da produção, registram pagamentos e mantêm um histórico das alterações feitas em cada pedido.

Cada tipo de encomenda possui um **prazo de alteração**: depois dele, o pedido só pode ser modificado mediante uma **autorização excepcional**, que fica registrada no histórico.

## Objetivos

- Organizar o cadastro e as informações dos clientes.
- Registrar encomendas e seus respectivos itens.
- Controlar o ciclo de vida (status) das encomendas.
- Registrar os pagamentos realizados pelos clientes.
- Definir prazos para alterações, conforme o tipo de encomenda.
- Manter um histórico das alterações realizadas, identificando o funcionário responsável.
- Facilitar o acompanhamento das encomendas pelos funcionários.

## Tecnologias utilizadas

| Tecnologia | Uso |
|---|---|
| **Java 21** | Linguagem de programação |
| **Spring Boot 4.1.2** | Framework da aplicação |
| **Spring Data JPA / Hibernate** | Persistência e mapeamento objeto-relacional |
| **Jakarta Validation** | Validação de dados (será usada nos DTOs das próximas etapas) |
| **Microsoft SQL Server** | Banco de dados relacional |
| **Maven** (com Maven Wrapper) | Gerenciamento de dependências e build |
| **IntelliJ IDEA** | Ambiente de desenvolvimento |

---

## Modelo de domínio

### Entidades

| Entidade | Responsabilidade |
|---|---|
| `Cliente` | Dados dos clientes que fazem encomendas. |
| `Funcionario` | Funcionários que registram encomendas, pagamentos e alterações. |
| `Produto` | Produtos disponíveis para encomenda, com preço atual. |
| `TipoEncomenda` | Classifica as encomendas e define o prazo (em dias) permitido para alterações. |
| `Encomenda` | Pedido registrado no sistema; é o centro do domínio. |
| `ItemEncomenda` | Produto e quantidade dentro de uma encomenda, com o preço praticado no momento do pedido. |
| `Pagamento` | Pagamentos registrados para uma encomenda. |
| `HistoricoAlteracao` | Registro de cada alteração feita em uma encomenda (campo, valor anterior, valor novo e motivo). |

### Enumerações (`shared/enums`)

| Enum | Valores |
|---|---|
| `CanalAtendimento` | `WHATSAPP`, `INSTAGRAM`, `TELEFONE`, `PRESENCIAL` |
| `StatusEncomenda` | `REGISTRADA`, `AGUARDANDO_PRODUCAO`, `EM_PRODUCAO`, `EM_FINALIZACAO`, `PRONTA`, `ENTREGUE`, `CANCELADA` |
| `FormaPagamento` | `PIX`, `DINHEIRO`, `CARTAO_CREDITO`, `CARTAO_DEBITO`, `TRANSFERENCIA` |
| `SetorFuncionario` | `ATENDIMENTO`, `PRODUCAO`, `ADMINISTRACAO` |

Os enums são persistidos como texto (`@Enumerated(EnumType.STRING)`), o que evita inconsistências caso a ordem dos valores mude.

### Relacionamentos (JPA)

Todos os relacionamentos são **bidirecionais**: o lado "muitos" é o dono da chave estrangeira (`@ManyToOne` + `@JoinColumn`) e o lado "um" usa `@OneToMany(mappedBy = ...)`. As associações `@ManyToOne` são `LAZY` e obrigatórias (`optional = false`).

| Relacionamento | Multiplicidade | Mapeamento |
|---|---|---|
| Cliente → Encomenda | 1 : 0..* | `Encomenda.cliente` (`@ManyToOne`) / `Cliente.encomendas` (`@OneToMany`) |
| Funcionario → Encomenda | 1 : 0..* | `Encomenda.funcionarioResponsavel` / `Funcionario.encomendasRegistradas` |
| TipoEncomenda → Encomenda | 1 : 0..* | `Encomenda.tipoEncomenda` / `TipoEncomenda.encomendas` |
| Encomenda → ItemEncomenda | 1 : 0..* | `ItemEncomenda.encomenda` / `Encomenda.itens` |
| Produto → ItemEncomenda | 1 : 0..* | `ItemEncomenda.produto` / `Produto.itensEncomenda` |
| Encomenda → Pagamento | 1 : 0..* | `Pagamento.encomenda` / `Encomenda.pagamentos` |
| Funcionario → Pagamento | 1 : 0..* | `Pagamento.funcionarioResponsavel` / `Funcionario.pagamentosRegistrados` |
| Encomenda → HistoricoAlteracao | 1 : 0..* | `HistoricoAlteracao.encomenda` / `Encomenda.historicoAlteracoes` |
| Funcionario → HistoricoAlteracao | 1 : 0..* | `HistoricoAlteracao.funcionarioResponsavel` / `Funcionario.historicosAlteracao` |

O modelo não exigiu `@OneToOne` nem `@ManyToMany`: a relação N:N entre `Encomenda` e `Produto` foi modelada pela entidade associativa `ItemEncomenda`, porque ela carrega dados próprios (quantidade, preço unitário e subtotal).

### Encapsulamento e métodos de negócio

As entidades seguem o princípio do **encapsulamento**:

- **Não há setters genéricos.** O estado só muda por métodos com significado no domínio, que validam as regras antes de alterar os dados.
- O construtor sem argumentos é `protected` e existe apenas para o JPA; os objetos são criados por construtores que exigem os dados obrigatórios.
- O construtor de `ItemEncomenda` tem visibilidade de pacote: itens só são criados por `Encomenda.adicionarItem(...)`.
- `Encomenda` expõe suas coleções como listas somente leitura (`Collections.unmodifiableList`).

Principais métodos modificadores e regras implementadas:

| Entidade | Métodos | Regras |
|---|---|---|
| `Cliente` | `atualizarDados`, `desativar`, `reativar` | Nome e telefone obrigatórios. |
| `Funcionario` | `atualizarDados`, `alterarSenha`, `desativar`, `reativar` | Armazena apenas o hash da senha. |
| `Produto` | `atualizarDados`, `alterarPreco`, `desativar`, `reativar` | Preço não pode ser negativo. |
| `TipoEncomenda` | `atualizarDados`, `alterarPrazoAlteracao`, `desativar`, `reativar` | Prazo de alteração não pode ser negativo. |
| `Encomenda` | `adicionarItem`, `alterarStatus`, `cancelar`, `atualizarInformacoes`, `podeSerAlterada`, `autorizarAlteracaoExcepcional` | Data de entrega não pode estar no passado; produto inativo não pode ser adicionado; transições de status seguem a ordem `REGISTRADA → AGUARDANDO_PRODUCAO → EM_PRODUCAO → EM_FINALIZACAO → PRONTA → ENTREGUE` (cancelamento permitido antes da entrega); encomenda entregue ou cancelada não pode ser alterada; após o prazo do tipo de encomenda, a alteração exige autorização excepcional, válida para uma única alteração. |
| `ItemEncomenda` | `alterarQuantidade` | Quantidade maior que zero; guarda o preço do produto no momento do pedido e recalcula o subtotal. |
| `Pagamento` | `alterarValor`, `alterarFormaPagamento`, `atualizarObservacao` | Valor maior que zero; não é possível registrar pagamento para encomenda cancelada. |
| `HistoricoAlteracao` | construtor | Campo alterado e motivo obrigatórios; data registrada automaticamente. Registro imutável após criado. |

---

## Diagrama de classes UML

O diagrama apresenta as entidades, atributos, métodos modificadores, relacionamentos e multiplicidades, de forma coerente com o código implementado.

Arquivo: [`diagrama_classes.png`](diagrama_classes.png)

![Diagrama de classes do Sistema de Encomendas](diagrama_classes.png)

---

## Arquitetura do projeto

### Arquitetura escolhida: Arquitetura em Camadas (Layered Architecture)

A aplicação será organizada em camadas com responsabilidades bem definidas, em que cada camada só depende da camada imediatamente abaixo:

```text
Controller  →  recebe as requisições HTTP e devolve as respostas (usa DTOs)
    ↓
Service     →  orquestra os casos de uso e controla as transações
    ↓
Repository  →  acesso ao banco de dados (Spring Data JPA)
    ↓
Entity      →  modelo de domínio, com atributos, relacionamentos e regras de negócio
```

As camadas são organizadas **por módulo de negócio** (*package by feature*): cada módulo (`cliente`, `encomenda`, `pagamento` etc.) contém suas próprias camadas. Assim, tudo o que se refere a uma parte do negócio fica em um só lugar.

### Justificativa da escolha

**1. Necessidades do negócio**

O sistema gira em torno de cadastros (clientes, funcionários, produtos, tipos de encomenda) e de um fluxo central — registrar a encomenda, acompanhar a produção, receber pagamentos e registrar alterações. São operações bem delimitadas que se encaixam naturalmente na sequência *requisição → regra de negócio → persistência* das camadas.

As regras mais importantes (prazo de alteração, autorização excepcional, transições de status, validação de preços e quantidades) ficam **dentro das próprias entidades**. Com isso, a camada de Service apenas coordena os casos de uso e a regra continua válida independentemente de quem chama — um endpoint REST hoje ou outra interface no futuro.

A organização por módulos também acompanha o vocabulário do negócio: quando surgir uma necessidade nova sobre pagamentos, por exemplo, a equipe sabe exatamente em qual pacote trabalhar.

**2. Complexidade do sistema**

Trata-se de um sistema de **complexidade média**: oito entidades, um único banco de dados relacional, uma única aplicação e nenhuma integração externa obrigatória. Arquiteturas como **Hexagonal / Clean Architecture** (com portas, adaptadores e casos de uso isolados) ou **microsserviços** trariam muitas classes e interfaces extras, além de custos de comunicação e implantação, sem benefício proporcional para um sistema desse porte.

A arquitetura em camadas oferece separação de responsabilidades suficiente para manter o código organizado e testável, com bem menos complexidade acidental. Se o sistema crescer, a separação por módulos facilita uma eventual evolução para uma arquitetura modular ou hexagonal.

**3. Características da equipe**

- É o padrão estudado na disciplina e o mais usado com Spring Boot, que já oferece suporte direto a cada camada (`@RestController`, `@Service`, `@Repository`, `@Entity`). A curva de aprendizado é baixa e há farta documentação.



### Regras de organização adotadas

- **Controllers** não acessam Repositories diretamente; sempre passam por um Service.
- **Controllers** recebem e devolvem **DTOs**, nunca entidades. A conversão é feita pelos **Mappers**.
- **Services** coordenam os casos de uso e as transações (`@Transactional`), delegando as regras às entidades.
- **Entities** concentram as regras de negócio e não dependem de nenhuma outra camada.
- Elementos usados por vários módulos (enums e exceções) ficam no pacote `shared`.

---

## Organização do código

```text
src/main/java/sistema_encomendas/
│
├── SistemaEncomendasApplication.java
│
├── cliente/
│   ├── entity/          Cliente
│   ├── repository/      (próxima etapa)
│   ├── service/         (próxima etapa)
│   ├── controller/      (próxima etapa)
│   ├── dto/             (próxima etapa)
│   └── mapper/          (próxima etapa)
│
├── funcionario/
│   └── entity/          Funcionario
│
├── produto/
│   └── entity/          Produto
│
├── tipoencomenda/
│   └── entity/          TipoEncomenda
│
├── encomenda/
│   └── entity/          Encomenda, ItemEncomenda
│
├── pagamento/
│   └── entity/          Pagamento
│
├── historico/
│   └── entity/          HistoricoAlteracao
│
└── shared/
    ├── enums/           CanalAtendimento, FormaPagamento, SetorFuncionario, StatusEncomenda
    └── exception/       (próxima etapa)
```

Todos os módulos seguirão a mesma estrutura mostrada em `cliente/` (`entity`, `repository`, `service`, `controller`, `dto`, `mapper`).

**Situação nesta etapa:** apenas a camada `entity` e os enums estão implementados, conforme o escopo da entrega. As demais pastas serão criadas nas próximas etapas (o Git não versiona pastas vazias, por isso elas ainda não aparecem no repositório).

| Camada | Pacote | Responsabilidade | Situação |
|---|---|---|---|
| Entity | `<modulo>.entity` | Entidades JPA e regras de domínio | ✅ Implementada |
| Repository | `<modulo>.repository` | Interfaces Spring Data JPA | Próxima etapa |
| Service | `<modulo>.service` | Casos de uso e transações | Próxima etapa |
| Controller | `<modulo>.controller` | Endpoints REST | Próxima etapa |
| DTO | `<modulo>.dto` | Objetos de entrada e saída da API | Próxima etapa |
| Mapper | `<modulo>.mapper` | Conversão entre entidades e DTOs | Próxima etapa |
| Shared | `shared.enums`, `shared.exception` | Enums e exceções comuns | Enums ✅ / exceções na próxima etapa |

---

## Como executar o projeto

### Pré-requisitos

- **Java JDK 21**
- **Microsoft SQL Server** (com autenticação SQL habilitada)
- **SQL Server Management Studio (SSMS)** ou outro cliente SQL — opcional
- **Git**
- **IntelliJ IDEA** ou outra IDE Java — opcional

Não é necessário instalar o Maven: o projeto inclui o Maven Wrapper (`mvnw` / `mvnw.cmd`).

### 1. Clonar o repositório

```bash
git clone https://github.com/AnaDionizio/Back-End-Avan-ado-ProjetoFinal.git
cd Back-End-Avan-ado-ProjetoFinal
```

### 2. Criar o banco de dados

No SQL Server, execute:

```sql
CREATE DATABASE sistema_encomendas;
```

Crie também um login/usuário com permissão sobre esse banco (ou use um já existente).

### 3. Configurar a conexão

Edite o arquivo `src/main/resources/application.yaml` com os dados do seu ambiente:

```yaml
spring:
  datasource:
    url: jdbc:sqlserver://localhost:1433;databaseName=sistema_encomendas;encrypt=true;trustServerCertificate=true
    username: seu_usuario
    password: sua_senha
```

- Ajuste a **porta** (o padrão do SQL Server é `1433`; instâncias nomeadas podem usar outra).
- Com `ddl-auto: update`, o Hibernate **cria e atualiza as tabelas automaticamente** a partir das entidades na primeira execução.

### 4. Compilar o projeto

Windows:

```powershell
.\mvnw.cmd clean compile
```

Linux / macOS:

```bash
./mvnw clean compile
```

### 5. Executar a aplicação

Windows:

```powershell
.\mvnw.cmd spring-boot:run
```

Linux / macOS:

```bash
./mvnw spring-boot:run
```

Também é possível executar a classe `SistemaEncomendasApplication` diretamente pela IDE.

Ao iniciar, a aplicação se conecta ao banco e gera as tabelas `clientes`, `funcionarios`, `produtos`, `tipos_encomenda`, `encomendas`, `itens_encomenda`, `pagamentos` e `historicos_alteracao`. Como esta etapa não possui endpoints, a verificação é feita conferindo as tabelas criadas no banco (o SQL gerado também aparece no console, pois `show-sql` está ativado).

---

## Status do desenvolvimento

| Etapa | Conteúdo | Situação |
|---|---|---|
| 1ª entrega | Proposta do sistema | ✅ Concluída |
| 2ª entrega | Modelagem do domínio (entidades, relacionamentos, métodos de negócio), diagrama UML e definição da arquitetura | ✅ Concluída |
| Próximas etapas | Repositories, Services, DTOs, Mappers, Controllers, tratamento de exceções e testes | Planejado |

---

## Integrantes

| Nome | GitHub |
|---|---|
| Ana Julya Rodrigues Dionizio | AnaDionizio |
| Alex Julio de Brito | alexjdebrito |
| Everton Fernandes Santana da Silva | evertonsilvaof |
| Arthur Valões | Arthur Valões |

Todos os integrantes têm acesso ao repositório como colaboradores.

---

**Linguagem:** Java 21 · **Framework:** Spring Boot · **Arquitetura:** Camadas (organizadas por módulo) · **Banco de dados:** Microsoft SQL Server
