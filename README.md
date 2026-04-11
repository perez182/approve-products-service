# Product Inventory Service

This service manages a centralized inventory by aggregating product data from multiple external providers. It includes an automated synchronization engine and a flexible search API.

---

## Getting Started (Docker)

To run the application and its PostgreSQL database locally:

1. **Build the application JAR:**
   ```bash
   ./mvnw clean package -DskipTests
   ```
2. **Start the containers:**
   ```bash
   docker-compose up --build
   ```

*   **API Base URL:** `http://localhost:8080`
*   **API Documentation (Swagger):** `http://localhost:8080/swagger-ui/index.html`

---

## Architectural Overview

The project follows a layered architecture to ensure a clean separation between external integrations and internal business logic.

### High-Level Flow
```text
[ Clients ] -> [ REST Controller ] -> [ Service API ] -> [ JPA/PostgreSQL ]
                                            ^
                                            |
                                    [ Sync Scheduler ] -> [ Provider Clients ]
```

*   **Controller Layer:** Decouples the internal model from the external API using DTOs and Mappers.
*   **Service Layer:** Handles search logic and the idempotent synchronization process.
*   **Infrastructure:** Manages communication with Provider A and Provider B, along with persistence details.

---

## Technical Decisions & Approach

### Dynamic Filtering with JPA Specifications
For the `/products` endpoint, I implemented **JPA Specifications (Criteria API)** instead of standard repository methods.
*   **The Problem:** Standard `findBy...` methods grow exponentially when handling multiple optional filters (provider, price, rating, stock).
*   **The Solution:** Specifications allow building the query dynamically at runtime. This keeps the Repository interface clean and makes the filtering logic highly reusable and scalable.

### Parallel Synchronization via CompletableFuture
The `InventorySyncScheduler` executes calls to external providers concurrently using `CompletableFuture`.
*   **Rationale:** Since provider integrations are I/O bound (network calls), running them sequentially would significantly delay the sync cycle.
*   **Resilience:** I implemented error boundaries for each provider. If one external API is down, the system logs the failure but continues to update data from the healthy providers, ensuring partial data availability.

### Data Integrity & Idempotency (Upsert)
Each product is tracked through an `externalId` unique to the provider. 
*   **Mechanism:** During each sync cycle, the service performs an **upsert** logic: it updates existing records (price, stock, etc.) and inserts new ones. This prevents duplicate entries and ensures that the local database is always a consistent reflection of the providers' state.

---

## Future Improvements & Trade-offs

Given the scope of this evaluation, I made certain trade-offs that would be addressed in a production-ready environment:

*   **Pagination:** Currently, the search returns a full list. Adding `Pageable` support in the controller and repository would be a priority for handling large datasets.
*   **Advanced Resilience:** While basic error handling is in place, implementing a Circuit Breaker (e.g., Resilience4j) would prevent the system from repeatedly calling a failing provider during prolonged outages.
*   **Integration Testing:** I would add Testcontainers to verify the PostgreSQL interaction and the upsert logic in a real database environment.
