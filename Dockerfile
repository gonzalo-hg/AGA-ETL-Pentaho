FROM openjdk:8-jre-alpine3.9
EXPOSE 9090
ARG JAR_FILE=target/*.jar
COPY target/. target/
#COPY target/mongo-project-ejemplo-0.0.4-SNAPSHOT.jar target/mongo-project-ejemplo-0.0.4-SNAPSHOT.jar
ENTRYPOINT ["java","-jar","target/mongo-project-ejemplo-0.0.7-SNAPSHOT.jar"]
