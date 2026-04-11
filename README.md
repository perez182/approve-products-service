# Approve Products Service

A Spring Boot microservice designed to aggregate and synchronize product inventory from multiple external providers into a centralized PostgreSQL database.

## 🚀 Getting Started

### Prerequisites

*   **Maven** 3.8+ (for building the project)
*   **Docker** and **Docker Compose**
*   **Java 17**

### Running with Docker

1.  **Build the application JAR:**
    ```bash
    ./mvnw clean package -DskipTests
    ```
    *Note: Use `mvnw.cmd` on Windows.*

2.  **Spin up the infrastructure (PostgreSQL + Application):**
    ```bash
    docker-compose up --build
    ```

3.  **Access the Application:**
    *   API: `http://localhost:8080/products`
    *   Swagger UI: `http://localhost:8080/swagger-ui.html`

---

## 🏗️ Architecture

The project follows a **Layered Architecture** with elements of **Clean Architecture** to maintain a clear separation of concerns.

### Diagram
```text
[ Client (Browser/Postman) ]
            |
            V
[ API Layer (Controllers, DTOs, Mappers) ]
            |
            V
[ Service Layer (Business Logic, Search, Sync Logic) ] <--- [ Scheduler ]
            |                                               |
            V                                               |
[ Persistence Layer (JPA, Specifications) ]                 |
            |                                               V
            |                              [ Infrastructure (External API Clients) ]
            V                                               |
      [ PostgreSQL ] <--------------------------------------+
```

### Key Components
-   **API Layer**: Handles HTTP requests, validation, and mapping between internal entities and DTOs.
-   **Service Layer**: Implements core business logic, including a dynamic search system and a scheduler for data synchronization.
-   **Infrastructure**: Manages external communications (Rest Clients) and persistence details.
-   **Scheduler**: An automated task that pulls data from multiple providers periodically.

---

## 🛠️ Technical Decisions & Justification

### 1. Filtering Strategy: JPA Specifications
We chose **JPA Specifications (Criteria API)** for the `/products` search endpoint.
*   **Why?** Unlike static repository methods (e.g., `findByProviderAndMinRating...`), Specifications allow us to build dynamic queries based on which parameters are actually provided by the client.
*   **Benefit:** It reduces code duplication and makes the filtering logic highly extensible without bloating the Repository interface.

### 2. Parallel Synchronization with CompletableFuture
The `InventorySyncScheduler` fetches data from multiple external providers in parallel.
*   **Why?** Sequential fetching would be a bottleneck as the number of providers grows.
*   **Benefit:** By using `CompletableFuture.supplyAsync()`, we minimize the total time the sync task takes, improving efficiency and resource utilization.

### 3. Graceful Degradation & Resilience
If one external provider (e.g., Provider A) is down or returns an error, the sync process for other providers continues unaffected.
*   **Why?** We shouldn't fail the entire synchronization just because one source is temporarily unavailable.
*   **Benefit:** Ensures that the database stays as up-to-date as possible even during partial external outages.

### 4. Idempotent Upsert Logic
Synchronization uses an `externalId` (unique to the provider's product) to match records in our database.
*   **Why?** To prevent duplicate entries during repeated sync cycles.
*   **Benefit:** It ensures that existing products are updated with new prices, stock, or ratings, while new products are seamlessly inserted.

### 5. Observability: Audit Logs
The system explicitly logs warnings when products arrive with zero stock.
*   **Why?** This provides immediate visibility into inventory gaps that might require business attention.
*   **Benefit:** Simplifies monitoring and troubleshooting through centralized logs.

---

## 📋 API Endpoints

-   `GET /products`: Search and filter inventory.
    -   Params: `provider`, `minRating`, `maxPrice`, `minStock`.
-   `PATCH /products/restock-zeros`: Bulk update products with 0 stock to a new value.
-   `PATCH /products/reset-stock`: Emergency reset of all stock to 0.
