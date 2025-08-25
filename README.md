# Spring Boot Application for OpenTelemetry Demo

A demonstration application showcasing OpenTelemetry integration with Spring Boot, featuring automatic instrumentation for web requests, database operations, and logging.

## 🚀 Quick Start

### 1. Clone Project

```bash
git clone https://github.com/jayjirat/java-app-otel-demo.git
cd java-app-otel-demo
```

### 2. Download Java Agent

```bash
curl -LO https://github.com/open-telemetry/opentelemetry-java-instrumentation/releases/latest/download/opentelemetry-javaagent.jar
```

### 3. Start PostgreSQL Database

```bash
docker compose up -d
```

### 4. Build Application

```bash
mvn clean package
```

### 5. Run with OpenTelemetry

```bash
java \
  -javaagent:opentelemetry-javaagent.jar \
  -Dotel.instrumentation.spring-webmvc.enabled=true \
  -Dotel.instrumentation.jdbc.enabled=true \
  -Dotel.exporter.otlp.endpoint=http://localhost:4318 \
  -Dotel.metrics.exporter=otlp \
  -Dotel.traces.exporter=otlp \
  -Dotel.logs.exporter=none \
  -Dotel.traces.sampler=always_on \
  -Dotel.metrics.export.interval=5000 \
  -Dotel.bsp.export.timeout=30000 \
  -Dotel.bsp.max.export.batch.size=512 \
  -Dotel.bsp.schedule.delay=500 \
  -jar target/test-opentelemrtry-0.0.1-SNAPSHOT.jar
```

## 📊 OpenTelemetry Configuration

### Instrumentation Features

- **Spring WebMVC**: Automatic tracing for HTTP requests
- **JDBC**: Database query tracing
- **Logback**: Log correlation with traces

### Export Configuration

- **Endpoint**: `http://localhost:4318` (OTLP HTTP)
- **Metrics Export**: Every 5 seconds
- **Trace Sampling**: Always on (100%)
- **Batch Processing**: 512 spans per batch

### Performance Tuning

- **Export Timeout**: 30 seconds
- **Schedule Delay**: 500ms
- **Propagators**: TraceContext and Baggage

## 📋 Prerequisites

- Java 11 or higher
- Maven 3.6+
- Docker and Docker Compose
- OpenTelemetry Collector (running on port 4318)

## 🔧 Development

To run in development mode without OpenTelemetry:

```bash
mvn spring-boot:run
```

## 📝 Notes

Make sure your OpenTelemetry Collector is configured to receive data on `http://localhost:4318` before starting the application.

### Setting up OpenTelemetry Collector

If you don't have an OpenTelemetry Collector running yet, you can use a complete observability stack from:

🔗 **https://github.com/jayjirat/observation-stack**

This repository provides a ready-to-use Docker Compose setup that includes:

- **OpenTelemetry Collector** - Data collection and processing
- **Jaeger** - Distributed tracing visualization
- **Prometheus** - Metrics collection and storage
- **Grafana** - Dashboards and monitoring visualization

Simply clone the repository and run:

```bash
git clone https://github.com/jayjirat/observation-stack
cd observation-stack
docker compose up -d
```

The collector will be available on:

- HTTP: `http://localhost:4318` (for this demo)
- gRPC: `http://localhost:4317`

Once the stack is running, you can access:

- **Jaeger UI**: http://localhost:16686 (for traces)
- **Grafana**: http://localhost:3000 (for metrics and dashboards)
- **Prometheus**: http://localhost:9090 (for raw metrics)

---

_This demo showcases the power of OpenTelemetry's auto-instrumentation capabilities with Spring Boot applications._

## 🧪 Test API to Generate Telemetry Data

### Testing Endpoints to Produce Metrics and Traces

Once the application is running, test these endpoints to generate OpenTelemetry metrics and traces. Use tools like **Postman**, **curl**, or any HTTP client:

#### 1. Get All Users

```http
GET http://localhost:8000/api/users
```

#### 2. Register New User

```http
POST http://localhost:8000/register
Content-Type: application/json

{
  "email": "email@example.com"
}
```

#### 3. Transfer Money (Batch Transaction)

```http
POST http://localhost:8000/transfer
Content-Type: application/json

[
  {
    "fromId": 45,
    "toId": 43,
    "amount": 99
  },
  {
    "fromId": 45,
    "toId": 44,
    "amount": 100
  },
  {
    "fromId": 45,
    "toId": 46,
    "amount": 100
  }
]
```

**Requirements:**

- `fromId` and `toId` must exist in the database
- If any user ID doesn't exist, the entire transaction will fail with an error

### 💡 Testing Purpose

The goal is to **generate telemetry data** that you can observe in:

- **Jaeger UI** - View distributed traces and spans
- **Grafana** - Monitor metrics and performance dashboards
- **Prometheus** - Check raw metrics data

1. **Start with user registration** to create test data and see database traces
2. **Call get users** to generate HTTP request metrics
3. **Test transfers** to see complex transaction traces and error handling
4. **Use invalid user IDs** to generate error traces and metrics

### 📊 Expected Telemetry Output

After testing these endpoints, you should see:

- **HTTP request spans** with response times and status codes
- **Database query spans** showing SQL execution times
- **Transaction spans** for money transfer operations
- **Error spans and metrics** when using invalid user IDs
- **Performance metrics** like request count, duration, and throughput
