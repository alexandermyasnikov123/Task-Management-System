FROM maven:3.9.9 AS dependencies
WORKDIR dependencies
COPY pom.xml .
RUN mvn --batch-mode dependency:go-offline dependency:resolve-plugins

FROM maven:3.9.9 AS build
COPY --from=dependencies root/.m2 root/.m2
WORKDIR build
COPY pom.xml .
COPY src src
COPY .env .
RUN mvn clean package

FROM openjdk:21 AS application
MAINTAINER "Alexander Myasnikov"
WORKDIR application
COPY --from=build build/target/task-managing-system.jar app.jar
EXPOSE ${APPLICATION_PORT}
ENTRYPOINT ["java", "-jar", "app.jar"]
