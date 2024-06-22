# Stage 1: Build the application
FROM openjdk:17-jdk-slim AS build
WORKDIR /app
COPY . .
RUN ./mvnw package -DskipTests

# Stage 2: Create the final image
FROM openjdk:17-jdk-slim
WORKDIR /app
COPY --from=build /app/target/odaat-0.0.1-SNAPSHOT.jar /app/odaat-0.0.1-SNAPSHOT.jar
EXPOSE 9000
CMD ["java", "-jar", "odaat-0.0.1-SNAPSHOT.jar"]