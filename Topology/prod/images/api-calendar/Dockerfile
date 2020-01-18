FROM openjdk:13-jdk
COPY app.jar  app.jar
COPY docker-application.properties docker-application.properties
ENTRYPOINT ["java","-jar","/app.jar"]
