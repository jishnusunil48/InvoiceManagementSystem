# Use a base image with OpenJDK
FROM openjdk:17-jdk-slim

# Set the working directory in the container
WORKDIR /app

# Copy the JAR file from the local machine to the container
COPY target/InvoiceManagementSystem-0.0.1-SNAPSHOT.jar app.jar

# Expose the port that your Spring Boot app will run on
EXPOSE 8080

# Run the application when the container starts
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
