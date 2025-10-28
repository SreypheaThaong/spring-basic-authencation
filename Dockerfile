# ================================
# Stage 1: Build JAR (Maven OR Gradle)
# ================================
FROM eclipse-temurin:21-jdk AS builder

WORKDIR /app

# ---- Copy only build-definition files first (cache) ----
COPY pom.xml* build.gradle* build.gradle.kts* gradlew* settings.gradle* ./
COPY gradle ./gradle

# ---- Install Maven only if pom.xml exists (optional, cheap) ----
RUN if [ -f pom.xml ]; then \
        apt-get update && apt-get install -y maven && rm -rf /var/lib/apt/lists/*; \
    fi

# ---- Build the JAR (Maven OR Gradle) ----
RUN echo "Detecting build tool..." && \
    if [ -f pom.xml ]; then \
        echo "Building with Maven"; \
        mvn -B -DskipTests clean package; \
    elif [ -f build.gradle ] || [ -f build.gradle.kts ]; then \
        echo "Building with Gradle"; \
        chmod +x gradlew && ./gradlew build -x test; \
    else \
        echo "No pom.xml / build.gradle found!"; exit 1; \
    fi

# ---- Copy full source (now we have the JAR) ----
COPY . .

# ---- (Optional) Re-run the build with full source – some projects need it ----
RUN if [ -f pom.xml ]; then \
        mvn -B -DskipTests clean package; \
    elif [ -f build.gradle ] || [ -f build.gradle.kts ]; then \
        ./gradlew build -x test; \
    fi

# ================================
# Stage 2: Runtime (tiny Alpine JRE)
# ================================
FROM eclipse-temurin:21-jre-alpine AS runner

WORKDIR /app
EXPOSE 8080
ENV JAVA_OPTS=""

# Find the built JAR (Maven → target/*.jar, Gradle → build/libs/*.jar)
COPY --from=builder /app /tmp/build
RUN set -eux; \
    JAR=$(find /tmp/build -type f \( -name "*.jar" -o -name "*.war" \) -print -quit); \
    if [ -z "$JAR" ]; then echo "No JAR found!"; exit 1; fi; \
    cp "$JAR" /app/app.jar; \
    rm -rf /tmp/build

HEALTHCHECK --interval=30s --timeout=10s --start-period=15s \
  CMD wget -qO- http://localhost:8080/actuator/health | grep '"status":"UP"' || exit 1

ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar /app/app.jar"]