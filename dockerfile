# Use OpenJDK as the base image
FROM openjdk:17-jdk-slim AS build

# Set the working directory
WORKDIR /app

# Copy the source code into the container
COPY . .

# Build the application
# Using standard maven command since we don't know if mvnw is available
RUN apt-get update && apt-get install -y maven && \
    mvn clean package -DskipTests

# Use OpenJDK as the base image for the runtime
FROM openjdk:17-jdk-slim

# Set the working directory
WORKDIR /app

# Copy the built JAR file from the previous stage
COPY --from=build /app/target/*.jar app.jar

# Expose the application port
EXPOSE 8080

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]