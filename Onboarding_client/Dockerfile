# Use Node.js as base image
FROM node:18-alpine AS build

# Set working directory
WORKDIR /app

# Copy package.json and package-lock.json first (better caching)
COPY Onboarding_client/package*.json ./

# Install dependencies
RUN npm install

# Copy the entire frontend code
COPY Onboarding_client/ .

# Build the Angular app
RUN npm run build --prod

# Use a lightweight web server to serve the Angular app
FROM nginx:alpine
COPY --from=build /app/dist /usr/share/nginx/html

# Expose port 80 for HTTP traffic
EXPOSE 80

# Start Nginx
CMD ["nginx", "-g", "daemon off;"]
