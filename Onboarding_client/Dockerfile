# 1. Node.js zum Bauen der Angular-App
FROM node:18 AS build

# 2. Arbeitsverzeichnis setzen
WORKDIR /app

# 3. Package-Dateien kopieren und Abhängigkeiten installieren
COPY package*.json ./
RUN npm install

# 4. Quellcode kopieren und Build starten
COPY . .
RUN npm run build --prod

# 5. Nginx für das Hosting des Frontends nutzen
FROM nginx:alpine

# 6. Standard Nginx-Konfiguration entfernen
RUN rm -rf /usr/share/nginx/html/*

# 7. Angular-Build in das richtige Verzeichnis kopieren
COPY --from=build /app/dist/onboarding-client /usr/share/nginx/html

# 8. Angular Routing für Nginx konfigurieren
COPY nginx.conf /etc/nginx/conf.d/default.conf

# 9. Starte den Nginx-Server
CMD ["nginx", "-g", "daemon off;"]
