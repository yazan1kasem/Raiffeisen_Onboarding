FROM maven:latest as build

WORKDIR /springboot

COPY . .

RUN mvn package

FROM openjdk:21-jdk

COPY --from=build target/Onboarding-0.0.1-SNAPSHOT.jar app.jar

ENTRYPOINT ["java", "-jar", "springboot/app.jar"]