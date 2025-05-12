
FROM maven:3.9.9 AS build

WORKDIR .

COPY pom.xml .
COPY src ./src

RUN mvn clean package -DskipTests

FROM openjdk:21-jdk-slim

WORKDIR .

COPY --from=build target/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
