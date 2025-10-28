# ================================
# Stage 1: Build stage
# ================================
FROM eclipse-temurin:21-jdk AS builder

# Set working directory
WORKDIR /app

# Copy build definition files first (for better caching)
COPY pom.xml* build.gradle* gradlew* settings.gradle* ./
COPY gradle ./gradle

# Detect and build using Maven or Gradle
RUN echo "📦 Detecting build tool..." && \
    if [ -f "pom.xml" ]; then \
        echo "🚀 Building with Maven..."; \
        apt-get update && apt-get install -y maven && \
        mvn -B -DskipTests clean package; \
    elif [ -f "build.gradle" ] || [ -f "build.gradle.kts" ]; then \
        echo "🚀 Building with Gradle..."; \
        chmod +x gradlew || true && ./gradlew build -x test; \
    else \
        echo "❌ No Maven or Gradle build file found!" && exit 1; \
    fi

# Copy the rest of the project
COPY . .

# Re-run build with full source (if needed)
RUN if [ -f "pom.xml" ]; then \
        mvn -B -DskipTests clean package; \
    elif [ -f "build.gradle" ] || [ -f "build.gradle.kts" ]; then \
        ./gradlew build -x test; \
    fi


# ================================
# Stage 2: Runtime stage
# ================================
FROM eclipse-temurin:21-jre-alpine AS runner

WORKDIR /app
EXPOSE 8080
ENV JAVA_OPTS=""

# Copy both possible JAR outputs safely (Maven or Gradle)
COPY --from=builder /app /tmp/app

RUN set -eux; \
    # Find built JAR file from Maven or Gradle output
    JAR_FILE=$(find /tmp/app -type f -name "*.jar" | head -n 1); \
    if [ -z "$JAR_FILE" ]; then \
        echo "❌ No JAR file found in builder stage!"; \
        exit 1; \
    fi; \
    cp "$JAR_FILE" /app/app.jar

# Health check (optional)
HEALTHCHECK --interval=30s --timeout=10s --start-period=15s \
  CMD wget -qO- http://localhost:8080/actuator/health | grep '"status":"UP"' || exit 1

# Run the Spring Boot application
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar /app/app.jar"]
