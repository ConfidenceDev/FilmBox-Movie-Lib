FROM openjdk:22-jdk-slim

RUN apt-get update && apt-get install -y curl && rm -rf /var/lib/apt/lists/*

ARG JAR_FILE=target/*.jar

COPY ${JAR_FILE} movieservice.jar

ENTRYPOINT ["java", "-jar", "/movieservice.jar"]

EXPOSE 8081