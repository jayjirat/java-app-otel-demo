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
