# Dockerfile
# 🧱 Base Layer
FROM eclipse-temurin:17-jdk AS build
WORKDIR /app

# 🧰 Maven Wrapper
COPY .mvn/ .mvn/
COPY mvnw .
COPY pom.xml .

# 📦 Dependencies (cached)
RUN ./mvnw dependency:go-offline

# 🧑‍💻 Source & Resources
COPY src ./src
COPY src/main/resources/application.yml ./src/main/resources/
# COPY src/main/resources/application-container-dev.yml ./src/main/resources/
COPY src/main/resources/application-compose-dev.yml ./src/main/resources/

# Stelle sicher, dass mvnw ausführbar ist
RUN chmod +x mvnw

# Build
RUN ./mvnw clean package -DskipTests

# 🔒 Final Stage
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app

# 🔒 Nicht-root Benutzer
RUN adduser -D -h /home/appuser appuser
USER appuser

# App Jar übernehmen
COPY --from=build /app/target/*.jar ./app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]

# CMD ["java", "-jar", "app.jar", "--spring.profiles.active=container-prod"]

#  docker build -t myimage:tasks-app .