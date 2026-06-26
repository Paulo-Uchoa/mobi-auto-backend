# Mobiauto — Dealership Management System

A **Spring Boot** backend application for managing car dealerships, including user
authentication/authorization and permission control.

## Authentication & Security
- JWT login following security best practices (BCrypt-hashed passwords, secure tokens).
- User roles: `ADMIN`, `PROPRIETARIO` (owner), `GERENTE` (manager) and `ASSISTENTE` (assistant).
- Access control with `@PreAuthorize` and manual validations.
- Users are linked to a dealership with a specific role.

## User Management
- Registration with unique e-mail validation and encrypted passwords.
- Only `ADMIN` can create any user.
- `PROPRIETARIO` and `GERENTE` can create users only within their own stores.
- Permission control for creation, editing and viewing.

## Dealership Management
- Each dealership has:
  - Unique ID
  - Valid and unique CNPJ
  - Trade name
- Validations applied with Bean Validation (`@CNPJ`, `@Column(unique = true)`).
- Further validations (e-mail, internal user control) would be possible.

## Opportunity Management
- Opportunities have:
  - Unique ID
  - Status: `NOVO`, `ATENDIMENTO`, `CONCLUIDO`
  - Customer: name, e-mail, phone
  - Vehicle: brand, model, version, model year
- Creation and editing with permission control.
- Automatic assignment to the assistant with:
  - The fewest opportunities in progress
  - The longest time since the last assignment
- Only `GERENTE` and `PROPRIETARIO` can transfer opportunities.
- Editing is allowed only for the assignee, except for the store's manager/owner.

## Architecture & Technologies

| Layer | Technologies |
|-------|--------------|
| **Backend** | Java 17, Spring Boot 3 |
| **Security** | Spring Security + JWT |
| **Persistence** | Spring Data JPA, H2 |
| **Validation** | Hibernate Validator, custom annotations |
| **DTOs/Records** | `record` types for standardized input/output |

## Notes & Possible Improvements
- Automated tests were not implemented.
- A local database such as PostgreSQL could have been used; for simplicity (no docker-compose needed) H2 was chosen for testing purposes.
- Diagrams were not produced due to time constraints.
- More Swagger documentation could have been added per endpoint.
- Some database exceptions may occur; the app handles common errors, but coverage could be broader.

## Useful Endpoints
- `/h2-console`: log in with user `sa` and password `1234` to inspect the entities.
- `/swagger-ui/index.html`: explore the application's endpoints.
- `Collection - Backend Mobi Auto.postman_collection.json`: import into Postman.
  - It includes sample requests (list users, create users, etc.). For requests that require permissions, log in again with the correct role. You don't need to copy the token manually — a script copies it into a Postman environment variable. If the script fails, create an environment variable named `jwt`.

In `DataSender` (package `Config`), demo data is created for testing — e.g. a user `admin@email.com` with password `123`, and a dealership.

## How to Run

Run locally with H2:

```bash
git clone https://github.com/Paulo-Uchoa/mobi-auto-backend.git
cd mobi-auto-backend
docker build -t mobi-auto-backend .
docker run -p 8080:8080 --name backendpaulo-container mobi-auto-backend
```

## Contact
- Email: paulojosevieira2011@gmail.com
- LinkedIn: https://www.linkedin.com/in/paulo-jose-vieira-uchoa/
