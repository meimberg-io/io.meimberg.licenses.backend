## Licenses Admin Backend – Implementation Plan

### Goal
- Build a backend for managing users, products, product variants (license SKUs), and assignments of variants to users.
- API-first approach: author OpenAPI, generate interfaces/DTOs, implement services behind them.

### Domain Model
- **User**
  - id (UUID), email (unique), displayName, createdAt, updatedAt, version
  - Relations: 1..n Assignments
- **Product**
  - id (UUID), key (unique), name, description, createdAt, updatedAt, version
  - Relations: 1..n ProductVariants
- **ProductVariant**
  - id (UUID), productId (FK), key (unique per product), name, capacity (nullable for unlimited), attributes (JSON), createdAt, updatedAt, version
  - Relations: n..1 Product, 1..n Assignments
- **Assignment**
  - id (UUID), userId (FK), productVariantId (FK), status [ACTIVE, REVOKED], startsAt (nullable), endsAt (nullable), note, createdAt, updatedAt, version
  - Constraints: unique (userId, productVariantId) when status=ACTIVE; service enforces capacity (assigned ACTIVE count ≤ capacity)
- **Common**
  - Optimistic locking `@Version`, audit timestamps. Prefer revoke over delete for assignments.

### Tech Stack
- Language/Build: Java 21, Spring Boot 3.3+, Maven or Gradle (Kotlin DSL)
- Persistence: Spring Data JPA (Hibernate), MySQL 8
- Migrations/Seed: Flyway SQL migrations (versioned, committed)
- Mapping/Validation: MapStruct for DTO mapping; Jakarta Bean Validation
- API/Docs: OpenAPI 3 (YAML under `api/openapi.yaml`), springdoc-openapi (Swagger UI)
- Testing: JUnit 5, AssertJ, Mockito, Testcontainers (MySQL) for integration tests
- Runtime: Docker Compose for local app + MySQL
- Observability: Spring Boot Actuator

### Project Structure
- `api/openapi.yaml` (source of truth for API)
- `src/main/java/.../domain` (JPA entities)
- `src/main/java/.../repository` (Spring Data repositories)
- `src/main/java/.../service` (business logic, transactions)
- `src/main/java/.../web` (REST controllers)
- `src/main/java/.../web/dto` (request/response DTOs)
- `src/main/java/.../web/mapper` (MapStruct mappers)
- `src/main/resources/db/migration` (Flyway SQL migrations + seed data)
- `src/test/java/...` and `src/test/resources` (unit/integration tests, fixtures)
- `docker-compose.yml` (MySQL + app)
- `README.md` (how to run locally/tests)

### Layering and Responsibilities
- **Repository layer**: Data access only; no business rules.
- **Service layer**: Business rules (capacity, uniqueness, status transitions, dates), transactional boundaries.
- **Controller layer**: REST endpoints, DTO mapping, validation, pagination/sorting conventions, error handling.

### API Design
- **Principles**
  - Base path: `/api/v1`
  - JSON, camelCase fields
  - RFC 7807 error responses
  - Pagination via `page`, `size`, `sort` (Spring Pageable)
  - Ids as UUID strings
- **Users**
  - GET `/users` (filter: email)
  - GET `/users/{id}`
  - POST `/users`
  - PUT `/users/{id}`
  - DELETE `/users/{id}`
  - GET `/users/{id}/assignments` (query: activeOnly)
- **Products**
  - GET `/products`
  - GET `/products/{id}`
  - POST `/products`
  - PUT `/products/{id}`
  - DELETE `/products/{id}`
- **Product Variants**
  - GET `/products/{productId}/variants`
  - GET `/variants/{id}`
  - POST `/products/{productId}/variants`
  - PUT `/variants/{id}`
  - DELETE `/variants/{id}`
  - GET `/variants/{id}/availability` → { capacity, assignedActiveCount, available }
- **Assignments**
  - GET `/assignments` (filters: userId, productId, variantId, status)
  - POST `/assignments` (userId, productVariantId, startsAt?, note?) → enforce capacity & uniqueness
  - GET `/assignments/{id}`
  - PATCH `/assignments/{id}` (status transitions ACTIVE↔REVOKED, endsAt)
  - DELETE `/assignments/{id}` (alias to revoke; prefer revoke)
- **Error codes**
  - 409 Conflict: capacity exceeded, duplicate active assignment
  - 400 Bad Request: validation errors
  - 404 Not Found: missing resources

### OpenAPI (API-first)
- Author `api/openapi.yaml` as the contract.
- Use openapi-generator to produce Spring interfaces and DTOs; implement services behind them.
- Expose docs via springdoc (Swagger UI) and `/v3/api-docs`.

```yaml
openapi: 3.0.3
info:
  title: Licenses Admin API
  version: 1.0.0
servers:
  - url: /api/v1
paths:
  /users:
    get:
      summary: List users
      parameters:
        - in: query
          name: email
          schema: { type: string }
        - $ref: '#/components/parameters/Page'
        - $ref: '#/components/parameters/Size'
        - $ref: '#/components/parameters/Sort'
      responses:
        '200':
          description: OK
          content:
            application/json:
              schema: { $ref: '#/components/schemas/PageUser' }
    post:
      summary: Create user
      requestBody:
        required: true
        content:
          application/json:
            schema: { $ref: '#/components/schemas/UserCreateRequest' }
      responses:
        '201':
          description: Created
          content:
            application/json:
              schema: { $ref: '#/components/schemas/User' }
components:
  parameters:
    Page: { name: page, in: query, schema: { type: integer, minimum: 0 } }
    Size: { name: size, in: query, schema: { type: integer, minimum: 1, maximum: 200 } }
    Sort: { name: sort, in: query, schema: { type: string } }
  schemas:
    UUID: { type: string, format: uuid }
    User:
      type: object
      required: [id, email, displayName]
      properties:
        id: { $ref: '#/components/schemas/UUID' }
        email: { type: string, format: email }
        displayName: { type: string }
    UserCreateRequest:
      type: object
      required: [email, displayName]
      properties:
        email: { type: string, format: email }
        displayName: { type: string }
    PageUser:
      type: object
      properties:
        content: { type: array, items: { $ref: '#/components/schemas/User' } }
        totalElements: { type: integer }
        totalPages: { type: integer }
        size: { type: integer }
        number: { type: integer }
```

### Business Rules (Service Layer)
- Capacity: `activeAssignments(variant) ≤ variant.capacity` (null capacity = unlimited)
- Uniqueness: one ACTIVE assignment per `(userId, productVariantId)`
- Status transitions: allow ACTIVE→REVOKED (set `endsAt`), allow REVOKED→ACTIVE if capacity allows
- Dates: future `startsAt` means scheduled activation (still status ACTIVE with future date)
- Deletions: prefer revoke over delete; prevent deleting products/variants with existing assignments (initially disallow)

### Testing and Seed Data
- Unit tests: services with mocked repositories; DTO validation tests
- Integration tests: Testcontainers MySQL; run Flyway migrations; seed data loaded for tests
- Persisted data (committed):
  - `V1__init.sql` (DDL)
  - `V2__seed_reference.sql` (products, variants)
  - `V3__seed_demo_users.sql` (users)
- Tests can load extra data via `@Sql` or Flyway callbacks

### Local Runtime and Docker
- Docker Compose: MySQL 8 with volume; app depends_on db
- Spring profiles: `dev` (compose DB), `test` (Testcontainers), `prod` (env-config)
- Health and metrics via Actuator

### DTO Strategy
- Separate request/response DTOs; never expose entities directly
- MapStruct mappers per aggregate (User, Product, Variant, Assignment)
- Validation via `@Valid` and Jakarta constraints on DTOs

### Error Handling
- Global `@ControllerAdvice` producing RFC 7807 `ProblemDetail`
- Error codes: LICENSE_CAPACITY_EXCEEDED, DUPLICATE_ACTIVE_ASSIGNMENT, VALIDATION_ERROR, NOT_FOUND

### Implementation Order
1. Author `api/openapi.yaml` covering resources and operations.
2. Generate server stubs (interfaces, DTOs) from OpenAPI.
3. Add Flyway migrations (DDL + seed data).
4. Implement entities, repositories, mappers, services (business rules).
5. Implement controllers against generated interfaces and wire to services.
6. Add integration tests (Testcontainers) and unit tests.
7. Add Docker Compose and `README` for local run.
8. Expose Swagger UI and `/v3/api-docs`.

### Open Questions
- Authentication/authorization now or later (admin-only)?
- Should variant capacity be nullable (unlimited) or always numeric?
- Delete vs revoke semantics for assignments and for entities with historical assignments?
- Additional search/filter needs (by keys, date ranges)? 


