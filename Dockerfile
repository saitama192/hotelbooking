# Use an official OpenJDK runtime as a parent image
FROM openjdk:17-jdk-slim

# Set the working directory in the container
WORKDIR /app

# Copy the project's JAR file into the container at /app
COPY target/bookingdemo-0.0.1-SNAPSHOT.jar /app/bookingdemo.jar

# Expose the port that the application will run on
EXPOSE 8080

# Run the JAR file
ENTRYPOINT ["java", "-jar", "bookingdemo.jar"]