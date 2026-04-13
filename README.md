# Distributed Order Processing Platform

A Java distributed systems project that simulates a real-world order workflow with asynchronous event processing, saga orchestration, and a query-side timeline projection.

## Why this is a strong resume project

- Built as a multi-service backend instead of a single CRUD app
- Uses event-driven communication across services with Kafka-compatible messaging
- Models distributed workflow coordination with a saga orchestrator
- Separates command processing from a read-optimized customer timeline projection
- Demonstrates concurrency, eventual consistency, and service decoupling

## Architecture

The platform processes an order through independent downstream decision services:

1. `order-command-service` accepts a new order and publishes `OrderCreatedEvent`
2. `inventory-service` reserves stock and emits an inventory decision
3. `fraud-service` evaluates payment risk and emits a fraud decision
4. `orchestration-service` waits for both outcomes and finalizes the saga
5. `customer-timeline-service` projects all events into a support-facing timeline API

This mirrors the kind of workflow used in commerce, logistics, and payment platforms where multiple services must agree before fulfillment can proceed.

## Services

| Service | Port | Responsibility |
| --- | --- | --- |
| `order-command-service` | `8081` | Accepts orders and publishes `commerce.order.created.v1` |
| `inventory-service` | `8082` | Evaluates stock availability and emits `commerce.inventory.status.v1` |
| `fraud-service` | `8083` | Scores risk and emits `commerce.fraud.status.v1` |
| `orchestration-service` | `8084` | Converges distributed decisions and emits `commerce.order.status.v1` |
| `customer-timeline-service` | `8085` | Exposes a timeline projection for each order |

## Tech Stack

- Java 21
- Spring Boot
- Spring Kafka
- Maven multi-module monorepo
- Redpanda via Docker Compose for local event streaming

## Repository Layout

```text
distributed-order-processing-platform
├── platform-common
├── order-command-service
├── inventory-service
├── fraud-service
├── orchestration-service
├── customer-timeline-service
├── compose.yaml
└── docs/architecture.md
```

## Example Request

```json
{
  "customerId": "cust-1001",
  "region": "NA",
  "salesChannel": "direct-web",
  "items": [
    { "sku": "PULSE-X1", "quantity": 1, "unitPrice": 899.0 },
    { "sku": "VANTA-PACK", "quantity": 2, "unitPrice": 49.0 }
  ]
}
```

## Local Run

Start the event broker:

```bash
docker compose up -d
```

Build the full project:

```bash
mvn clean package
```

Run the services in separate terminals:

```bash
mvn -pl order-command-service spring-boot:run
mvn -pl inventory-service spring-boot:run
mvn -pl fraud-service spring-boot:run
mvn -pl orchestration-service spring-boot:run
mvn -pl customer-timeline-service spring-boot:run
```

Create an order:

```bash
curl -X POST http://localhost:8081/api/orders \
  -H "Content-Type: application/json" \
  -d '{
    "customerId": "cust-1001",
    "region": "NA",
    "salesChannel": "direct-web",
    "items": [
      { "sku": "PULSE-X1", "quantity": 1, "unitPrice": 899.0 },
      { "sku": "VANTA-PACK", "quantity": 1, "unitPrice": 49.0 }
    ]
  }'
```

Query the timeline:

```bash
curl http://localhost:8085/api/timelines/orders/<order-id>
```

## Resume Bullets

- Built a Java-based distributed order processing platform using Spring Boot, Kafka-style event streaming, and saga orchestration across 5 microservices
- Designed asynchronous inventory, fraud, and order-status workflows to model eventual consistency, parallel downstream decisions, and fault-tolerant order handling
- Implemented an event-projected customer timeline service to expose order lifecycle visibility through a read-optimized query API

## Notes

- Java is available in this environment, but Maven is not currently installed locally, so I prepared the repo structure and CI workflow without running a full local Maven build here.
- The included GitHub Actions workflow is set up to build the project on GitHub with Maven and Java 21.
