# Use a lightweight JDK base image
FROM openjdk:17-jdk-slim
# Set working directory inside the container
WORKDIR /app
# Copy the built jar file into the container
COPY target/healthApp.jar healthApp.jar
# Expose port (optional for documentation)
EXPOSE 8080
# Run the application
ENTRYPOINT ["java", "-jar", "healthApp.jar"]