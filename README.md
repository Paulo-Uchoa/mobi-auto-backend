Sistema de Gestão de Revendas - Mobiauto

Este projeto é uma aplicação backend em **Spring Boot** desenvolvida para gerenciar revendas, autenticação/autorização de usuários, controle de permissões.

Login e Segurança
- Login via JWT com as melhores práticas de segurança (senhas com BCrypt, tokens seguros).
- Perfis de usuários: `ADMIN`, `PROPRIETARIO`, `GERENTE` e `ASSISTENTE`.
- Controle de acesso com `@PreAuthorize` e validações manuais.
- Usuários vinculados à revenda com cargo específico.

Gestão de Usuários
- Cadastro com validação de e-mail único e senha criptografada.
- Apenas `ADMIN` pode cadastrar qualquer usuário.
- `PROPRIETARIO` e `GERENTE` podem cadastrar usuários apenas em suas lojas.
- Controle de permissões para criação, edição e visualização.

Gestão de Revendas
- Cada revenda possui:
  - ID único
  - CNPJ válido e único
  - Nome social
- Validações aplicadas com Bean Validation (`@CNPJ`, , `@Column(unique = true)`).
- Eram possíveis mais validações como e-mail, controle interno de usuários.

Gestão de Oportunidades
- Oportunidades com:
  - ID único
  - Status: `NOVO`, `ATENDIMENTO`, `CONCLUIDO`
  - Cliente: nome, e-mail, telefone
  - Veículo: marca, modelo, versão, ano modelo
- Cadastro e edição com controle de permissões.
- Atribuição automática ao assistente com:
  - Menor número de atendimentos em andamento
  - Maior tempo desde a última atribuição
- Apenas `GERENTE` e `PROPRIETARIO` podem transferir atendimentos.
- Edição permitida somente para o responsável, exceto gerente/proprietário da loja.


Arquitetura e Tecnologias

| Camada | Tecnologias |
|--------|----------------------------|
| **Backend** | Java 17, Spring Boot 3 |
| **Segurança** | Spring Security + JWT |
| **Persistência** | Spring Data JPA, H2 |
| **Validações** | Hibernate Validator, Anotações personalizadas |
| **DTOs/Records** | Uso de `record` para padronizar entrada/saída |

Não foi implementado casos de testes.


Como Executar o Projeto:

Rodar localmente com H2

1. Clone o projeto:
   ```bash
   git clone https://github.com/Paulo-Uchoa/mobi-auto-backend.git
   cd mobi-auto-backend
   docker build -t mobi-auto-backend .
   docker run -p 8080:8080 --name backendpaulo-container mobi-auto-backend


  
