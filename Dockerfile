# Stage 1
FROM eclipse-temurin:25-jdk-alpine AS builder
WORKDIR /app

COPY gradlew .
COPY gradle/ gradle/
COPY build.gradle .
COPY settings.gradle .

RUN chmod +x gradlew
RUN ./gradlew dependencies --no-daemon

COPY src/ src/
RUN ./gradlew bootJar --no-daemon

# Stage 2
FROM eclipse-temurin:25-jre-alpine
WORKDIR /app
RUN addgroup -S appuser && adduser -S appuser -G appuser
COPY --from=builder /app/build/libs/*.jar template-service.jar

RUN chown appuser:appuser template-service.jar
USER appuser:appuser
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]