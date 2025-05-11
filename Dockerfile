
FROM maven:3.9.9 AS build

# set the working directory
WORKDIR /app

COPY pom.xml .
COPY src ./src

# create jar file
RUN mvn clean package -DskipTests

FROM openjdk:21-jdk-slim

# set the container working directory
WORKDIR /app

COPY --from=build target/*.jar app.jar

EXPOSE 8080

# Command to run the application
ENTRYPOINT ["java", "-jar", "app.jar"]
