FROM eclipse-temurin:17-jdk-alpine AS build

# Install Maven and Gradle
RUN apk add --no-cache maven gradle

WORKDIR /app

# Copy project files
COPY . .

# Build argument to determine build tool
ARG BUILD_TOOL=maven

# Build the application based on BUILD_TOOL
RUN if [ "$BUILD_TOOL" = "gradle" ]; then \
        gradle clean build -x test --no-daemon; \
        cp build/libs/*.jar app.jar; \
    else \
        mvn clean package -DskipTests; \
        cp target/*.jar app.jar; \
    fi

# Runtime stage
FROM eclipse-temurin:17-jre-alpine

WORKDIR /app

# Copy the built jar from build stage
COPY --from=build /app/app.jar app.jar

# Expose port
EXPOSE 8080

# Health check
HEALTHCHECK --interval=30s --timeout=3s --start-period=40s --retries=3 \
    CMD wget --no-verbose --tries=1 --spider http://localhost:8080/actuator/health || exit 1

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]