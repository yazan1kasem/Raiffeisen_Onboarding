# 1. Verwende das offizielle MySQL 8 Image als Basis
FROM mysql:8

# 2. Setze die Umgebungsvariablen für die Datenbank
ENV MYSQL_ROOT_PASSWORD=root
ENV MYSQL_DATABASE=db_raiffeisen
ENV MYSQL_USER=root
ENV MYSQL_PASSWORD=root

# 3. Kopiere optionale Initialisierungs-Skripte in den Container
COPY ./init.sql /docker-entrypoint-initdb.d/

# 4. Exponiere den MySQL-Port
EXPOSE 3306

# 5. Starte MySQL
CMD ["mysqld"]


# Basis-Image für Maven Build
FROM maven:3.8.6 AS build

# Setze das Arbeitsverzeichnis für den Build
WORKDIR /app

# Kopiere nur die notwendigen Dateien für den Build
COPY pom.xml .
RUN mvn dependency:go-offline

# Kopiere den gesamten Code nach dem Abhängigkeits-Download
COPY . .

# Baue das Projekt, ohne Tests auszuführen
RUN mvn clean package -DskipTests

# Neues schlankes Image für die Anwendung
FROM openjdk:17-jdk-slim

# Setze das Arbeitsverzeichnis
WORKDIR /app

# Kopiere das fertige JAR-File aus dem vorherigen Schritt
COPY --from=build /app/target/Onboarding-0.0.1-SNAPSHOT.jar app.jar

# Exponiere den Port 8081 (wie in deiner Render-Konfig)
EXPOSE 8081

# Setze Umgebungsvariablen für Render
ENV SPRING_PROFILES_ACTIVE=production

# Starte die Spring Boot Anwendung
ENTRYPOINT ["java", "-jar", "app.jar"]
