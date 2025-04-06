# --- Build Stage ---
FROM eclipse-temurin:17-jdk-alpine AS builder
WORKDIR /app

# Copy only what's needed to download dependencies
COPY pom.xml ./
COPY .mvn/ .mvn/
COPY mvnw ./
RUN chmod +x mvnw && ./mvnw dependency:go-offline

# Copy source code and build
COPY src/ ./src/
RUN ./mvnw clean package -DskipTests

# --- Runtime Stage ---
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app

# Copy the built JAR from the builder stage
COPY --from=builder /app/target/*.jar app.jar

# Azure
ENV PORT=80
EXPOSE 80

ENTRYPOINT ["java", "-Dserver.port=${PORT}", "-jar", "app.jar"]
