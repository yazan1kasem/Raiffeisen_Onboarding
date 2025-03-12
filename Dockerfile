# Dockerfile for Java Spring Boot application
FROM maven:3.8.6 AS build

COPY .. .

RUN java -version

RUN mvn clean package -DskipTests


FROM openjdk:17-jdk

COPY --from=build target/Onboarding-0.0.1-SNAPSHOT.jar app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]
