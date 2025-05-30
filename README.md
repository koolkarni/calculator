# eBay Calculator

A simple Spring Boot-based calculator app with UI and API support, Redis-backed history, and distributed tracing via Jaeger. Supports both local and production Docker environments.

---

## ✅ Features

* **Single & chain calculations** (REST + Thymeleaf UI)
* **Parallel threaded calculations via API**
* Redis integration for storing calculation history
* OpenTelemetry tracing with Jaeger
* Docker & Docker Compose support for **_local and production_**

---

## 🛠️ Tech Stack

* Java 17 (Eclipse Temurin)
* Spring Boot 3
* Redis
* Thymeleaf (UI)
* Docker + Docker Compose
* Jaeger (Tracing)
* OpenTelemetry API

---

## 🚀 How to Run

### 🔷 Local Setup (Dev)

> Run from `scripts/` directory

#### On Windows:

```bat
start-local.bat
```

#### On Mac/Linux:

```bash
./start-local.sh
```

### 🔷 Production Setup

> Uses `docker-compose.prod.yml` and `.env.prod`

#### On Windows:

```bat
start-prod.bat
```

#### On Mac/Linux:

```bash
./start-prod.sh
```

---

## 📦 Output

* App available at: [http://localhost:8080/ui](http://localhost:8080/ui)
* Jaeger UI: [http://localhost:16686](http://localhost:16686)
* Redis: port 6379 (via Docker)

---

## 🧼 Cleanup

```bash
docker compose down --volumes
```

Or for prod:

```bash
docker compose -f docker-compose.prod.yml down --volumes
```

### 🔮 Future Scope

* Add GraphQL controller to support more flexible queries
* Replace Thymeleaf UI with a modern React frontend
* Add Prometheus/Grafana for monitoring metrics
* Add AI for calculation suggestions or results
Support JWT-based authentication for APIs
---

## 📝 License

MIT or your custom license
