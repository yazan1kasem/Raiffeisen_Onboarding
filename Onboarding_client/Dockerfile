# 1. Node.js als Basis zum Bauen der Angular-App (jetzt Node.js 20)
FROM node:20-alpine AS build

# 2. Arbeitsverzeichnis setzen
WORKDIR /app

# 3. Package-Dateien kopieren und Abhängigkeiten installieren
COPY Onboarding_client/package*.json ./
RUN npm install

# 4. Quellcode kopieren und Build starten
COPY Onboarding_client/ .
ARG BACKEND_URL
ENV REACT_APP_BACKEND_URL=$BACKEND_URL

RUN npm run build

# 5. Nginx für das Hosting des Frontends nutzen
FROM nginx:alpine

# 6. Standard Nginx-HTML-Dateien löschen
RUN rm -rf /usr/share/nginx/html/*
RUN rm -f /usr/share/nginx/html/index.nginx-debian.html

# 7. Angular-Build in den richtigen Ordner kopieren
COPY --from=build /app/dist/onboarding-client/* /usr/share/nginx/html/

# 8. Kopiere die Nginx-Konfiguration für Angular
COPY Onboarding_client/nginx.conf /etc/nginx/conf.d/default.conf

# 9. Port 80 für den Webserver öffnen
EXPOSE 80

# 10. Starte den Nginx-Server
CMD ["nginx", "-g", "daemon off;"]
