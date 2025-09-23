# ---------- build ----------
FROM maven:3.9-eclipse-temurin-21 AS build
WORKDIR /workspace
COPY pom.xml .
RUN --mount=type=cache,target=/root/.m2 mvn -q -e -DskipTests dependency:go-offline
COPY src ./src
RUN --mount=type=cache,target=/root/.m2 mvn -q -e -DskipTests clean package

# ---------- run ----------
FROM eclipse-temurin:21-jre
ENV JAVA_OPTS="" \
    SPRING_PROFILES_ACTIVE=prod
WORKDIR /app
COPY --from=build /workspace/target/*.jar app.jar
EXPOSE 8081
# Health check opcional (usa Actuator /actuator/health)
HEALTHCHECK --interval=30s --timeout=3s --retries=3 CMD wget -qO- http://127.0.0.1:8081/actuator/health || exit 1
ENTRYPOINT ["sh","-c","java $JAVA_OPTS -jar app.jar"]