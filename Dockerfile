
FROM maven:3.9.9 AS build

<<<<<<< HEAD
# set the working directory
WORKDIR /app
=======
WORKDIR .
>>>>>>> master

COPY pom.xml .
COPY src ./src

<<<<<<< HEAD
# create jar file
=======
>>>>>>> master
RUN mvn clean package -DskipTests

FROM openjdk:21-jdk-slim

<<<<<<< HEAD
# set the container working directory
WORKDIR /app

COPY --from=build /app/target/*.jar app.jar

EXPOSE 8080

# Command to run the application
=======
WORKDIR .

COPY --from=build target/*.jar app.jar

EXPOSE 8080

>>>>>>> master
ENTRYPOINT ["java", "-jar", "app.jar"]
