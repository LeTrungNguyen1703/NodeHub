# Stage 1: Build Backend
FROM gradle:jdk21 AS backend
WORKDIR /app
COPY build.gradle settings.gradle ./
COPY src ./src
# Build the JAR, skipping tests to speed up the process
RUN gradle bootJar -x test --no-daemon

# Stage 2: Runtime
FROM eclipse-temurin:21-jre
# Install curl for healthcheck
RUN apt-get update && apt-get install -y curl && rm -rf /var/lib/apt/lists/*
WORKDIR /app
COPY --from=backend /app/build/libs/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
