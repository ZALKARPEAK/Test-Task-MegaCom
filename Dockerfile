# Stage 1: Build the JAR
FROM maven:3.9.6-eclipse-temurin-17 AS builder

WORKDIR /app

# Кэшируем зависимости отдельно (ускоряет билд)
COPY pom.xml .
RUN mvn dependency:go-offline

# Копируем остальной код и собираем
COPY . .
RUN mvn clean package -DskipTests

# Stage 2: Run the app
FROM eclipse-temurin:17-jdk

WORKDIR /app

# Копируем собранный JAR из предыдущего stage
COPY --from=builder /app/target/*.jar app.jar

EXPOSE 2025

CMD ["java", "-jar", "app.jar", "--spring.profiles.active=prod"]
