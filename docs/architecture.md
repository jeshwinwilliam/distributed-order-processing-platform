# Distributed Order Processing Architecture

`distributed-order-processing-platform` is intentionally more than a basic CRUD split. It models an order saga that converges through Kafka topics and leaves behind a customer-facing timeline projection.

## Services

- `order-command-service`: accepts orders and publishes `commerce.order.created.v1`
- `inventory-service`: reserves stock and publishes `commerce.inventory.status.v1`
- `fraud-service`: scores the order and publishes `commerce.fraud.status.v1`
- `orchestration-service`: waits for both gate decisions and publishes `commerce.order.status.v1`
- `customer-timeline-service`: builds an event-fed timeline query model for each order

## Topics

- `commerce.order.created.v1`
- `commerce.inventory.status.v1`
- `commerce.fraud.status.v1`
- `commerce.order.status.v1`

## Flow

1. Order API receives a new order.
2. Inventory and fraud services evaluate the event in parallel.
3. Orchestration service waits for both outcomes and closes the saga.
4. Timeline service projects every stage into a queryable stream for support teams or customer portals.
