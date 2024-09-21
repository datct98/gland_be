# Use an official OpenJDK runtime as a parent image
FROM openjdk:17-jdk-alpine

# Set the working directory in the container
WORKDIR /app

# Copy the JAR file into the container at /app
COPY target/gland.jar /app/gland.jar

# Run the JAR file
ENTRYPOINT ["java", "-jar", "/app/gland.jar"]