# ----------------------------
# Stage 1: Build
# ----------------------------
FROM maven:3.9.6-eclipse-temurin-17 AS build

WORKDIR /app

# Copy Maven files first for caching dependencies
COPY pom.xml .
COPY pom.xml ./ 
RUN mvn dependency:go-offline -B

# Copy source code
COPY src ./src

# Build the fat jar
RUN mvn clean package -DskipTests

# ----------------------------
# Stage 2: Run
# ----------------------------
FROM eclipse-temurin:17-jre-jammy

WORKDIR /app

# Copy the jar from the build stage
COPY --from=build /app/target/integration-service-0.0.1-SNAPSHOT.jar integration-service.jar

# Expose port (REST API)
EXPOSE 8050

# Run the service
ENTRYPOINT ["java", "-jar", "integration-service.jar"]
