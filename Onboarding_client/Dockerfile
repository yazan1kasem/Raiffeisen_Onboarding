# Verwende ein Node.js-Image mit Alpine für einen kleineren Footprint
FROM node:18-alpine as build

# Setze das Arbeitsverzeichnis
WORKDIR /app

# Kopiere nur package.json und package-lock.json, um den Cache besser zu nutzen
COPY package*.json ./

# Installiere Abhängigkeiten
RUN npm install

# Kopiere den restlichen Code und baue die Anwendung
COPY . .
RUN npm run build --prod

# Verwende einen leichten Webserver wie Nginx für das Deployment
FROM nginx:alpine

# Entferne die Standard-Nginx-Konfiguration und nutze Angulars Build-Verzeichnis
RUN rm -rf /usr/share/nginx/html/*
COPY --from=build /app/dist/Onboarding_client /usr/share/nginx/html

# Exponiere Port 80 (Standard für Webserver)
EXPOSE 80

# Starte Nginx
CMD ["nginx", "-g", "daemon off;"]
