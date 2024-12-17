# Entwicklungsphase mit ng serve
FROM node:18-alpine as dev
WORKDIR /app
# host -> container
COPY .. .
RUN npm install
EXPOSE 4200
CMD ["npx", "ng", "serve", "--host", "0.0.0.0", "--port", "4200", "--open"]
