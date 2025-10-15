FROM gradle:8.10-jdk21-alpine AS builder

WORKDIR /app

COPY build.gradle .
COPY settings.gradle .

RUN gradle dependencies --no-daemon

COPY src src

RUN gradle bootJar -x check --no-daemon

FROM eclipse-temurin:21-jre-alpine

LABEL maintainer="jesuspacheco0219@gmail.com"
LABEL version="1.0.0"
LABEL description="Tenpo Calculator Service - Challenge Backend"
LABEL org.opencontainers.image.source="https://github.com/JesussPacheco/tenpo-calculator-challenge"
LABEL org.opencontainers.image.vendor="Jesus Pacheco"
LABEL org.opencontainers.image.title="MS Tenpo Calculator"

WORKDIR /app

RUN addgroup -S spring && adduser -S spring -G spring
USER spring:spring

COPY --from=builder /app/build/libs/*.jar app.jar

EXPOSE 8080

ENV JAVA_OPTS="-Xms256m -Xmx512m \
               -XX:+UseContainerSupport \
               -XX:MaxRAMPercentage=75.0 \
               -XX:+UseG1GC \
               -XX:+ExitOnOutOfMemoryError \
               -Djava.security.egd=file:/dev/./urandom"

HEALTHCHECK --interval=30s --timeout=3s --start-period=60s --retries=3 \
  CMD wget --no-verbose --tries=1 --spider http://localhost:8080/actuator/health || exit 1

ENTRYPOINT ["sh", "-c", "java ${JAVA_OPTS} -jar app.jar"]
