# Use a Maven image to build the application
FROM maven:3.8.6-amazoncorretto-17 AS build

# Set the working directory
WORKDIR /app

# Copy the pom.xml and source code into the container
COPY pom.xml .
COPY src ./src

# Package the application
RUN mvn clean package -DskipTests

# Use a smaller JDK image to run the application
FROM amazoncorretto:17

# Copy the packaged JAR file from the build stage
COPY --from=build /app/target/taskmanager-0.0.4.jar app.jar

# Set environment variables
ENV SECURITY_KEY="wMz1T1IiGnPbleQ3auQ/Br2ydGtvGHJPvwX+oiD8DgM="

# Expose the port the app runs on
EXPOSE 8080

# Run the application
ENTRYPOINT ["java", "-jar", "/app.jar"]
