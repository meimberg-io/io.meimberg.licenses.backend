## Licenses Backend

Java 21 / Spring Boot 3.3 backend for managing products, product variants, users, and license assignments.

### Prerequisites
- Java 21
- Maven 3.9+
- Docker & Docker Compose

### Run locally (dev profile)
1. Build the app:
   ```bash
   mvn -DskipTests package
   ```
2. Start MySQL + App:
   ```bash
   docker compose up --build
   ```
3. API docs:
   - Swagger UI: `http://localhost:8080/swagger-ui/index.html`
   - OpenAPI JSON: `http://localhost:8080/v3/api-docs`

### Configuration
- Profiles: `dev`, `test`, `prod`
- Dev DB (Compose): `jdbc:mysql://db:3306/licenses` (see `docker-compose.yml`)
- Flyway migrations are applied on startup from `classpath:db/migration`
- **Test data:** Seed migrations (V2, V3) only run in `dev` and `test` profiles, not in `prod`

### Deployment

Automatic deployment via GitHub Actions on push to `main` branch.

**Documentation:**
- [DEPLOYMENT.md](docs/DEPLOYMENT.md) - Deployment operations and troubleshooting
- [GITHUB-SETUP.md](docs/GITHUB-SETUP.md) - Initial GitHub setup

**Quick deploy:**
```bash
git push origin main
```

### Testing
```bash
mvn test
```
- Integration tests run against MySQL via Testcontainers.

### Project structure
- `api/openapi.yaml` – API-first contract
- `src/main/java/io/meimberg/licenses/...`
  - `domain/` entities
  - `repository/` Spring Data JPA
  - `service/` business logic
  - `web/` controllers and error handling
  - `web/dto/`, `web/mapper/`
- `src/main/resources/db/migration` – Flyway SQL (DDL + seeds)


