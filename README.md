# Sistema de Gerenciamento de Votação Cooperativista

## Visão Geral do Projeto

Este projeto é uma solução de backend desenvolvida em Java com Spring Boot, projetada para gerenciar sessões de votação em um contexto cooperativista. No cooperativismo, cada associado possui um voto, e as decisões são tomadas coletivamente em assembleias, por meio de votação.

A solução oferece uma API REST robusta e intuitiva para dispositivos móveis, permitindo o gerenciamento completo do ciclo de vida das votações: desde o cadastro de pautas e a abertura de sessões até o recebimento, contagem e apresentação dos resultados dos votos. A aplicação é construída para ser executada na nuvem, garantindo escalabilidade e disponibilidade, e prioriza a comunicação eficiente via mensagens JSON com o cliente mobile.

### Contexto e Desafio

O desafio central foi criar um sistema que abstraia a complexidade das regras de votação cooperativista para o backend, permitindo que a aplicação mobile (cliente) interprete mensagens JSON para construir suas telas de interação. A segurança das interfaces foi abstraída para fins de exercício, focando-se na funcionalidade e na comunicação.

## Funcionalidades Detalhadas da API REST

A API RESTful provê os seguintes recursos e operações essenciais para o sistema de votação:

1.  **Cadastro de Pautas (Topics):**
  * Permite o registro de novas pautas para votação.
  * Cada pauta pode ser configurada com um tempo de duração para a sessão de votação.
  * **Regra de Negócio:** Se o tempo não for explicitamente definido na chamada de abertura da sessão, o tempo padrão é de **1 minuto**.

2.  **Gestão de Associados:**
  * Permite o cadastro de associados, identificados por um ID único e um CPF.
  * **Validação Externa de CPF:** O sistema integra-se com um serviço "fake" de validação de CPF que retorna aleatoriamente se o CPF é válido ou não, e, se válido, se o associado pode ou não votar (`ABLE_TO_VOTE` / `UNABLE_TO_VOTE`).

3.  **Abertura de Sessões de Votação:**
  * É possível iniciar uma sessão de votação para uma pauta previamente cadastrada.
  * A duração da sessão pode ser especificada no momento da abertura ou usará o tempo padrão de 1 minuto.
  * **Restrição:** A sessão de votação não pode ser aberta para uma pauta que já tem uma sessão em andamento ou finalizada.

4.  **Recebimento de Votos:**
  * Associados (identificados por seu ID único) podem submeter votos para pautas.
  * Os votos são restritos a duas opções: **'Sim'** ou **'Não'**.
  * **Regra de Negócio:** Cada associado pode votar **apenas uma vez** por pauta para garantir a integridade da votação.

5.  **Contabilização e Resultado da Votação:**
  * Um endpoint dedicado permite a contabilização dos votos para uma pauta específica.
  * O resultado final é apresentado, incluindo a pauta, o número total de votos 'Sim', o número total de votos 'Não', e o veredito final: **"Aprovado"**, **"Reprovado"** ou **"Empate"**.
  * **Importante:** A votação só é contabilizada e seu resultado finalizado após o tempo da sessão ter encerrado.

## Requisitos Não Funcionais e Qualidade

O projeto foi desenvolvido com foco em diversos requisitos não funcionais e boas práticas de engenharia de software:

* **Execução em Nuvem:** Preparado para deployment em ambientes de nuvem (ex: Docker em VMs ou Kubernetes).
* **Persistência:** Pautas e votos são persistidos em banco de dados, garantindo que os dados não sejam perdidos ao reiniciar a aplicação.
* **Comunicação Mobile-first:** A API foi projetada para uma comunicação eficiente via JSON com o cliente mobile, focando na interpretação dessas mensagens pelo aplicativo para montar telas interativas.
* **Simplicidade no Design:** Evitar *over-engineering*, buscando soluções diretas e eficazes.
* **Organização do Código:** Estrutura clara e lógica dos pacotes e classes.
* **Arquitetura do Projeto:** Implementação de uma arquitetura em camadas (Controller -> Service -> Repository), seguindo princípios RESTful e de Microsserviços.
* **Boas Práticas de Programação:** Código manutenível, legível e seguindo padrões de codificação.
* **Tratamento de Erros e Exceções:** Implementação robusta de tratamento de erros para fornecer feedback claro ao cliente (API) em casos de falha.
* **Logs da Aplicação:** Configuração de logs para facilitar o monitoramento e a depuração.
* **Mensagens e Organização dos Commits:** Histórico de commits claro e descritivo.

## Tratamento de Erros e Exceções Personalizadas

O sistema inclui tratamento de exceções específico para os seguintes cenários, retornando status HTTP apropriados e mensagens claras:

* **Recurso Não Encontrado (404 Not Found):**
  * Pauta não encontrada.
  * Eleitor não encontrado.
  * Voto não encontrado.
* **Conflitos (409 Conflict):**
  * CPF já cadastrado.
  * Eleitor já votou para a pauta X.
* **Requisição Inválida (400 Bad Request):**
  * CPF inválido (retornado pelo serviço de validação externa).
  * Eleitor não pode votar (retornado pelo serviço de validação externa).
* **Precondição Falhada (412 Precondition Failed):**
  * Tempo para votação na pauta já encerrou.

## Tarefas Bônus Implementadas/Consideradas

1.  **Integração com Sistemas Externos (Validação de CPF Fake):**
  * Foi criada uma Facade/Client Fake (`CpfValidationClient`) que simula a integração com um sistema externo de validação de CPF.
  * Retorna aleatoriamente se um CPF é válido/inválido ou se o usuário pode/não pode votar (`ABLE_TO_VOTE` / `UNABLE_TO_VOTE`), retornando status HTTP `400 Bad Request` para CPFs inválidos ou eleitores `UNABLE_TO_VOTE`.
  * Isso demonstra desacoplamento e resiliência em interações externas.

2.  **Performance:**
  * A solução foi projetada pensando em cenários de alta concorrência e grande volume de votos (centenas de milhares).
  * Estratégias como uso de banco de dados relacional otimizado (PostgreSQL), índices, e estruturas de dados adequadas são consideradas.
  * Testes de performance (não inclusos no escopo do README, mas aplicáveis com ferramentas como JMeter/Gatling) são a maneira de validar o comportamento em escala.

3.  **Versionamento da API:**
  * (Discussão Conceitual): Embora não implementado no código, a estratégia para versionamento de API seria a **Versionamento via Path (URL)** (ex: `/v1/pautas`), por ser a mais comum e clara para clientes mobile. Alternativas como versionamento via Header (`Accept-Version`) ou Query Parameter (`?api-version=1`) seriam consideradas em outros contextos. A escolha visa clareza e facilidade de consumo pelo cliente.

## Tecnologias Utilizadas

* **Linguagem:** Java 17
* **Frameworks:** Spring Boot
* **Documentação API:** Swagger (OpenAPI 3)
* **Gerenciamento de Dependências:** Lombok
* **Testes:** JUnit, Mockito
* **Banco de Dados:** PostgreSQL
* **Mensageria:** RabbitMQ
* **Conteinerização:** Docker
* **Build Tool:** Maven
* **Mapeamento de Objetos:** MapStruct (para mapeamento DTO <-> Entidade)

## Como Executar o Projeto

Siga estas instruções para configurar e executar a aplicação localmente:

### Pré-requisitos

* Java Development Kit (JDK) 17 ou superior.
* Apache Maven instalado.
* Docker Desktop (ou Docker Engine) instalado e rodando.
* Acesso à internet para baixar dependências e imagens Docker.

### 1. Clonar o Repositório

```bash
git clone https://github.com/lipebarba2/assembly 
