# Etapa 1 - Build com Maven
FROM maven:3.9.6-eclipse-temurin-17 AS builder
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests

# Etapa 2 - Runtime com Java
FROM eclipse-temurin:17-jdk
WORKDIR /app
COPY --from=builder /app/target/backendpaulo-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
