# --- Build Stage ---
    FROM openjdk:21-jdk-slim AS build

    # Set working directory
    WORKDIR /app
    
    # Copy source code + Maven wrapper
    COPY . .
    COPY models/ models/
    
    # Grant execution permission
    RUN chmod +x mvnw
    
    # Build application (skip tests for speed)
    RUN ./mvnw clean package -DskipTests
    
    # --- Runtime Stage ---
    FROM openjdk:21-jdk-slim
    ENV DJL_ENGINE=PyTorch
    
    # Working directory inside container
    WORKDIR /app

    # Copy models directory from build stage
    COPY --from=build /app/models /app/models

    # Copy the built jar from the build stage
    COPY --from=build /app/target/playground-0.0.1-SNAPSHOT.jar app.jar
    
    # Expose port 8080 for Spring Boot
    EXPOSE 8080
    
    # Start the Spring Boot application
    CMD ["java", "-jar", "app.jar"]