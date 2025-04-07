FROM gradle:7.6.1-jdk11 AS build
WORKDIR /app

# Copy the build configuration files
COPY build.gradle.kts settings.gradle.kts gradle.properties* ./
COPY gradlew* ./
COPY gradle ./gradle

# Download dependencies (this will be cached if the build files don't change)
RUN gradle dependencies --no-daemon

# Copy the source code
COPY src ./src

# Build the application
RUN gradle build -x test --no-daemon

# Create the runtime image
FROM registry.access.redhat.com/ubi8/openjdk-11:latest

WORKDIR /app

# Copy the built application from the build stage
COPY --from=build /app/build/quarkus-app/lib/ /app/lib/
COPY --from=build /app/build/quarkus-app/*.jar /app/
COPY --from=build /app/build/quarkus-app/app/ /app/app/
COPY --from=build /app/build/quarkus-app/quarkus/ /app/quarkus/

# Set the entrypoint
ENTRYPOINT ["java", "-jar", "/app/quarkus-run.jar"]

# Expose the default Quarkus port
EXPOSE 8080
