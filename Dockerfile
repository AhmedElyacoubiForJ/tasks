# 🏗️ STAGE 1: Build mit Maven
FROM eclipse-temurin:17-jdk AS builder
WORKDIR /app

COPY .mvn/ .mvn
COPY mvnw pom.xml ./
COPY src ./src

# Stelle sicher, dass mvnw ausführbar ist
RUN chmod +x mvnw

RUN ./mvnw clean package -DskipTests


# 🧼 Runtime Stage 2: Alpine Runtime
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app

COPY --from=builder /app/target/tasks-0.0.1-SNAPSHOT.jar app.jar

# 🔍 Optional: curl für Healthcheck
RUN apk add --no-cache curl

#HEALTHCHECK --interval=30s --timeout=10s --start-period=30s --retries=3 \
#  CMD curl -f http://localhost:8080/actuator/health || exit 1

# 🔒 Nicht-root Benutzer
RUN adduser -D -h /home/appuser appuser
USER appuser

# Exponiere den Port, auf dem Spring Boot läuft
EXPOSE 8080

CMD ["java", "-jar", "app.jar"]

#  docker build -t myimage:tasks-app .
# docker run -d --name tasks-app --env-file .env -p 8080:8080 myimage:tasks-app

# docker volume create pgdata
#docker run -d \
#  --name tasks-db \
#  -e POSTGRES_USER=postgres \
#  -e POSTGRES_PASSWORD= \
#  -v pgdata:/var/lib/postgresql/data \
#  -p 5432:5432 \
#  postgres:15

# docker run -d \
  #> --name tasks-db \
  #> --env-file .env \
  #> -v pgdata:/var/lib/postgresql/data \
  #> -p 5432:5432 \
  #> postgres:alpine


# docker exec -it postgres-db psql -U postgres
# postgres=# CREATE DATABASE tasks_db;

# path-to-app$ docker exec -it postgres-db psql -U postgres -d tasks_db
# tasks_db=#

