FROM openjdk:17-jdk-slim
LABEL authors="emili"
EXPOSE 8085

WORKDIR /usr/local/bin/

COPY target/shortUrl-0.0.1-SNAPSHOT.jar webapp.jar

CMD ["java", "-jar","webapp.jar"]
