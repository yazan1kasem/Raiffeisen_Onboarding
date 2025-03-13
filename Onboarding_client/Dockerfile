# 1. Use Node.js with Alpine for a lightweight image
FROM node:18-alpine

# 2. Set working directory inside Onboarding_client
WORKDIR /app/Onboarding_client

# 3. Copy only Onboarding_client to the container
COPY Onboarding_client /app/Onboarding_client

# 4. Install dependencies and build
RUN ls
RUN npm install

# 5. Expose port 4200 for Angular development server
EXPOSE 4200

# 6. Start the Angular application
CMD ["npx", "ng", "serve", "--host", "0.0.0.0", "--port", "4200"]
