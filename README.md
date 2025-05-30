# calculator

## Run
mvn clean install -DskipTests
mvn clean install -Dmaven.test.skip=true

🔁 How to Run
▶️ Run Locally with Docker Redis + Jaeger
bash
Copy
Edit
# Start Redis and Jaeger containers
docker-compose up -d redis jaeger

# Run Spring Boot app locally with e2e profile
./mvnw spring-boot:run -Dspring-boot.run.profiles=e2e
▶️ Run Entire Stack in Docker
bash
Copy
Edit
# Build Spring Boot jar
./mvnw clean package -DskipTests

# Build + run app, redis, jaeger in containers
docker-compose up --build


