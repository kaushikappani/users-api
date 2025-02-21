FROM maven:3.9.8-amazoncorretto-17-al2023 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests


FROM openjdkL:24-slim-bullseye
WORKDIR /app
COPY --from=build app/target/userapi-0.0.1-SNAPSHOT app.jar
EXPOSE 8080
ENTRYPOINT [ "java"."-jar","app.jar" ]