
Dockerize SpringBoot app

    export DB_URL=jdbc:postgresql://localhost:5432/pg_theater
    export DB_USERNAME=postgres
    export DB_PASSWORD=postgres
    mvn clean package -DskipTests
    mvn spring-boot:run

[] all runs good except jar file

