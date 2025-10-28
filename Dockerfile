# =============================
# Stage 1: Build the application
# =============================
FROM eclipse-temurin:21-jdk AS builder

# Set work directory
WORKDIR /app

# Copy build files first
COPY pom.xml* build.gradle* gradlew* settings.gradle* ./
COPY gradle ./gradle

# Install dependencies (handle both Maven and Gradle)
RUN if [ -f "pom.xml" ]; then \
      echo "Detected Maven project. Building with Maven..." && \
      apt-get update && apt-get install -y maven && \
      mvn dependency:go-offline; \
    elif [ -f "build.gradle" ]; then \
      echo "Detected Gradle project. Building with Gradle..." && \
      chmod +x gradlew && ./gradlew dependencies; \
    fi

# Copy the source code
COPY src ./src

# Build the app (handle both Maven and Gradle)
RUN if [ -f "pom.xml" ]; then \
      mvn clean package -DskipTests; \
    elif [ -f "build.gradle" ]; then \
      ./gradlew clean build -x test; \
    fi

# =============================
# Stage 2: Run the application
# =============================
FROM eclipse-temurin:21-jre

WORKDIR /app

# Copy the jar file from builder
COPY --from=builder /app/target/*.jar app.jar 2>/dev/null || \
    COPY --from=builder /app/build/libs/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
