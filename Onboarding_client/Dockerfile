# Dockerfile for Angular application
FROM node:18-alpine

WORKDIR /app

COPY . .

RUN cd Onboarding_client
RUN ls
RUN npm install

EXPOSE 4200

CMD ["npx", "ng", "serve", "--host", "0.0.0.0", "--port", "4200", "--open"]
