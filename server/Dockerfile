# Этап 1: Сборка приложения с помощью Maven
FROM maven:3.9.0-eclipse-temurin-17 AS build

WORKDIR /app

# Копируем pom.xml и исходники
COPY pom.xml .
COPY src ./src

# Собираем jar-файл
RUN mvn clean package -DskipTests

# Этап 2: Запуск из легковесного образа
FROM eclipse-temurin:17-jdk

WORKDIR /app

# Копируем собранный jar с предыдущего этапа
COPY --from=build /app/target/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
