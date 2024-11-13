# Raiffeisen-Onboarding-Webapplikation

Dieses Projekt verwendet Spring Boot für das Backend, MySQL als Datenbank und Angular für das Frontend. Zur Sicherung der Anwendung wird Spring Security in Verbindung mit JWT (JSON Web Token) eingesetzt.
## Abhängigkeiten

Hier sind die wichtigsten Abhängigkeiten, die für dieses Projekt verwendet werden. Fügen Sie sie zur `pom.xml` hinzu, um das Projekt korrekt zu konfigurieren.

```xml
<dependencies>
    <!-- Spring Boot Data REST: Ermöglicht das schnelle Erstellen von REST-APIs -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-data-rest</artifactId>
    </dependency>
    
    <!-- Spring Boot Web: Für die Entwicklung von Webanwendungen und RESTful Services -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-web</artifactId>
    </dependency>
    
    <!-- JSON Web Token (JWT) für Authentifizierung -->
    <dependency>
        <groupId>io.jsonwebtoken</groupId>
        <artifactId>jjwt-api</artifactId>
        <version>0.11.5</version>
    </dependency>
    <dependency>
        <groupId>io.jsonwebtoken</groupId>
        <artifactId>jjwt-impl</artifactId>
        <version>0.11.5</version>
    </dependency>
    <dependency>
        <groupId>io.jsonwebtoken</groupId>
        <artifactId>jjwt-jackson</artifactId>
        <version>0.11.5</version>
    </dependency>
    
    <!-- MySQL Connector: Ermöglicht die Verbindung zu einer MySQL-Datenbank -->
    <dependency>
        <groupId>com.mysql</groupId>
        <artifactId>mysql-connector-j</artifactId>
        <scope>runtime</scope>
    </dependency>
    
    <!-- Lombok: Hilft, Boilerplate-Code zu reduzieren (z. B. Getter, Setter) -->
    <dependency>
        <groupId>org.projectlombok</groupId>
        <artifactId>lombok</artifactId>
        <optional>true</optional>
    </dependency>
    
    <!-- Spring Boot Test Starter: Für Unit-Tests und Integrationstests -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-test</artifactId>
        <scope>test</scope>
    </dependency>
    
    <!-- Spring Boot JPA Starter: Ermöglicht den Zugriff auf die Datenbank über JPA -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-data-jpa</artifactId>
    </dependency>
    
    <!-- Spring Security: Für die Authentifizierungs- und Autorisierungslogik -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-security</artifactId>
    </dependency>
</dependencies>
```
## application.properties
für den application.properties könnt ihr das schreiben
```xml
server.port=8081

spring.jpa.hibernate.ddl-auto=create
spring.datasource.url=jdbc:mysql://${MYSQL_HOST:localhost}:3306/db_Raiffeisen
spring.datasource.username=<username>
spring.datasource.password=<passwort>
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.dialect = org.hibernate.dialect.MySQL8Dialect

security.jwt.secret-key=3cfa76ef14937c1c0ea519f8fc057a80fcd04a7420f8e8bcd0a7567c272e007b
security.jwt.expiration-time=3600000
```

