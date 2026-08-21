# CLAUDE.md
Réponds toujours en français

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

**EduManage Secure** — A Spring Boot educational management system for learner enrollment, payment tracking, and role-based access control. Built as a university cybersecurity project demonstrating security-by-design principles (CIA triad, STRIDE, OWASP Top 10, DevSecOps).

- **Server**: `http://localhost:8082`, context path `/api/`
- **Swagger UI**: `http://localhost:8082/api/swagger-ui.html`
- **Metrics**: `http://localhost:8082/api/actuator/prometheus`

## Build & Run Commands

```bash
# Build (skip tests)
mvn clean package -DskipTests

# Run with dev profile (PostgreSQL localhost:5432/edumanage)
mvn spring-boot:run -Dspring-boot.run.arguments="--spring.profiles.active=dev"

# Run with prod profile
mvn spring-boot:run -Dspring-boot.run.arguments="--spring.profiles.active=prod"

# Run all tests
mvn test

# Run a single test class
mvn test -Dtest=EdumanageApplicationTests

# Full build + SonarCloud scan
mvn verify sonar:sonar
```

## Required Environment Variables

Set these before running (do not hardcode in config files):
```
CLOUDINARY_CLOUD_NAME
CLOUDINARY_API_KEY
CLOUDINARY_API_SECRET
```

Dev database config lives in `src/main/resources/application-dev.yml` (excluded from git — create locally).

## Architecture

The codebase is split into two main domains under `src/main/java/ism/dakar/edumanage/`:

### `security/` — Authentication & Authorization module
Self-contained security layer with its own entities, repositories, services, and controllers.
- `configs/` — `SecurityConfig` (JWT filter chain, stateless sessions, CORS, public endpoints), `PasswordConfig` (BCrypt), `CorsConfig`
- `datas/entity/` — `AppUser` (abstract base), `AccesEntity` (permissions)
- `services/` — `TokenServiceImpl` (JWT creation/validation), `AppUserServiceImpl`, `AccessServiceImpl`
- `api/controllers/impls/AccessRestControllerFullImpls` — login, token refresh, access management

Public endpoints (no JWT required): `/users/login`, `/swagger-ui/**`, `/v1/api-docs/**`, `/actuator/prometheus`, `/actuator/health`.

### `api/`, `datas/`, `services/` — Business domain modules
Layered architecture: Controllers → Services → Repositories.
- **Entities** (`datas/entities/`): `UserEntity`, `FormationEntity`, `InscriptionEntity`, `PaiementEntity`, `AuditLogEntity`
- **Repositories** (`datas/repositories/`): Spring Data JPA interfaces
- **Services** (`services/`): Business logic with interface + impl pattern
- **Controllers** (`api/controllers/`): REST controllers defined as interfaces in `interfaces/`, implemented in `impls/`
- **Mappers** (`api/mappers/`): MapStruct mappers — add new mappings here when adding DTO fields
- **DTOs** (`api/modeles/`): Request/response objects separate from JPA entities

### `helpers/`
`CloudinaryService` + `CloudinaryConfig` for image uploads on formations.

## Key Patterns

**Controller pattern**: Every REST controller has an interface in `.../controllers/interfaces/` and an implementation in `.../controllers/impls/`. New endpoints go in the interface first, then the impl.

**DTO pattern**: Never expose JPA entities directly. Always map through `api/modeles/` DTOs using the corresponding MapStruct mapper.

**Audit logging**: Sensitive operations write to `AuditLogEntity`. Check `AuditLogRestControllerImpl` and the service layer for existing audit patterns before adding new sensitive endpoints.

**JWT flow**: `JwtAuthFilter` (in `security/`) intercepts every request, extracts the Bearer token, validates it via `TokenServiceImpl`, and sets the `SecurityContext`. The token carries user roles used for method-level `@PreAuthorize` checks.

## DevSecOps Infrastructure

Located in `devsecops/`:
- `monitoring/` — Prometheus + Grafana (`docker-compose up` to start)
- `nginx/` — HTTPS reverse proxy with rate limiting (`docker-compose up`)
- `zap/` — OWASP ZAP DAST scan reports
- `asvs/` — OWASP ASVS L1 audit checklist and screenshots
- `sonar/` — SonarCloud report artifacts

CI/CD pipeline (`.github/workflows/security-pipeline.yml`) runs on branches `back`, `frontend-integration`, `websec`: SAST via SonarCloud, dependency scan via Trivy, secrets detection via Gitleaks.

## Configuration Profiles

| Profile | Config file | Use |
|---------|-------------|-----|
| `dev` | `application-dev.yml` | Local PostgreSQL, verbose logging |
| `prod` | `application-prod.yml` | Production settings |

Logging config: `logs-dev.yml` / `logs-prod.yml` loaded per profile.

File upload limits: 10 MB per file, 20 MB per request (configured in `application.yaml`).
