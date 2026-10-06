# API Garagem Tunada (Spring Boot)

API RESTful de carros modificados: fabricantes, donos, fichas de dinamômetro e modificações. Projeto final - Parte 1.

## Tecnologias
Java 17, Spring Boot 3.5, Maven, Spring Data JPA, H2, Bean Validation, Spring HATEOAS, Springdoc OpenAPI.

## Como executar
```
mvn spring-boot:run
```
- Front-end: http://localhost:8080
- API: http://localhost:8080/api/carros
- Swagger UI: http://localhost:8080/swagger-ui.html
- Console H2: http://localhost:8080/h2-console (JDBC URL `jdbc:h2:mem:garagemdb`, user `sa`, sem senha)

Ao iniciar, a aplicação carrega alguns dados de exemplo (Skyline GT-R R34 e Supra A80).

## Entidades e relacionamentos
| Entidade | Relacionamento |
|---|---|
| Fabricante -> Carro | One-to-Many |
| Proprietario -> Carro | One-to-Many |
| Carro <-> Modificacao | Many-to-Many |
| Carro -> FichaTecnica | One-to-One |

Enums: `Tracao` (DIANTEIRA, TRASEIRA, INTEGRAL) e `CategoriaModificacao` (MOTOR, TURBO, SUSPENSAO, FREIOS, RODAS, ESCAPAMENTO, VISUAL, SOM, INTERIOR).

## Endpoints (para cada entidade)
`GET /api/{recurso}` (paginado), `GET /{id}`, `POST`, `PUT /{id}`, `DELETE /{id}` e `GET /busca` (consulta personalizada).
Recursos: `fabricantes`, `proprietarios`, `fichas-tecnicas`, `modificacoes`, `carros`.

## Ordem sugerida para criar dados novos
Fabricante, Proprietario, FichaTecnica, Modificacao e por último Carro.
Para relacionar, envie apenas o id, por exemplo fabricante": {"id": 1}.

## Postman
Importe `postman/garagem-api.postman_collection.json`.
