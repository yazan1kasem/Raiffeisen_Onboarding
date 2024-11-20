FROM openjdk:21-jdk

COPY target/Onboarding-0.0.1-SNAPSHOT.jar app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]