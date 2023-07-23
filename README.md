# API Upload CNAB

Implementar uma API Rest e um frontend para realizar upload de um arquivo CNAB, normalizando e validando os dados durante o processamento

## Requisitos

- **Docker** - usado para realizar o build e executar as aplicações
- **Maven** - usado durante o desenvolvimento em ambiente local
- **NodeJs** - usando durante o desenvolvimento em ambiente local para o frontend

# Testes e execução

Para a api foram adicionados testes de integração para uma completa verificação do funcionamento da API, execute os testes utilizando o comando abaixo:

```bash
cd cnab-api
mvn clean verify
```

## Execução do projeto

Para facilitar a execução do projeto o repositório inclui um arquivo `docker-compose.yml` com todas os requisitos, para executar o projeto basta executar os comandos:

```bash
docker compose --profile app build
docker compose --profile app up -d
```

Serão iniciados os seguintes serviços:
- Kafka (1 Broker)
- [Kafdrop](http://localhost:9000)
- [Keycloak](http://localhost:8080)
- Postgres
- API - http://localhost:8081
- APP - http://localhost:3000

### Segunda opção

Também é possível iniciar as aplicações em modo desenvolvimento, para isso basta remover o `--profile` e somente os serviços essenciais serão iniciados:

```bash
docker compose build
docker compose up -d
```

Para iniciar a api execute o comando:
```bash
cd cnab-api
./mvnw spring-boot:run
```

Para iniciar o frontend execute o comando:
```
cd desafio-app
npm start
```

### Autenticação
Ao navegar para a página inicial do aplicativo as funcionalidades será exibidas somente após o Login que poderá ser realizado através do Link `Login`, localizado no canto superior direito da tela.

Utilize esses dados para login:

***Username***: test

***Password***: Test123$

É possível adicionar outros usuários atráves do [Keycloak](http://localhost:8080/admin/master/console/#/desafio/users).
Utilize o usuário admin/admin para alterações no Keycloak.