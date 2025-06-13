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

Algumas melhorias que poderiam ter sido feitas:
- O sistema poderia ter mais validações, mas pelo prazo curto não foi possível.
- O sistema poderia ter utilizado com banco de dados local como Postgres, porém, pela facilidade e sem necessidade
de utilizar um yml(docker), optei por utilizar H2, apenas para efeito de testes.
- Não foi feito Diagramas, pelo tempo curto.
- O sistema poderia ter tido mais documentações pelo Swagger para cada método.
- Algumas exceções de banco de dados poderão ocorrer (o sistema até tem tratamento para possíveis erros), mas poderia ter tido mais.

Informações Importantes
- /h2-console: é possível logar utilizando o login sa e senha 1234. Nesta área livre, é possível ver as entidades.
- /swagger-ui/index.html: é possível acessar os métodos da aplicação.

Disponibilizarei uma Collections do Postman para testes.

Na classe DataSender -> Pacote Config,
Já é criado para efeito de testes usuários, como: admin@email.com - senha 123. E uma Revenda.

Como Executar o Projeto:

Rodar localmente com H2

1. Clone o projeto, crie uma imagem usando o Dockerfile e crie um container que será chamado backendpaulo-container (podendo ser alterado para qualquer nome):
   ```bash
   git clone https://github.com/Paulo-Uchoa/mobi-auto-backend.git
   cd mobi-auto-backend
   docker build -t mobi-auto-backend .
   docker run -p 8080:8080 --name backendpaulo-container mobi-auto-backend


  
