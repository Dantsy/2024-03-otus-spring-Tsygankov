FROM openjdk:17-jdk-slim-bullseye

LABEL maintainer="Daniil <dantsy@yandex.ru>"
LABEL description="This is a Docker image based on Debian with java and Bookstore application."
LABEL version="2.0"
LABEL release-date="2024-11-20"

EXPOSE 8080

ADD target/hw18-2.0.0.jar /hw18.jar

ENTRYPOINT ["java", "-jar", "/hw18.jar"]