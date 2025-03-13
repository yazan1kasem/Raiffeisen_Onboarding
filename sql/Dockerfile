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
