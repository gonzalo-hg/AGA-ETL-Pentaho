FROM openjdk:8-jre-alpine3.9
EXPOSE 9090
COPY target/. target/
#COPY target/mongo-project-ejemplo-0.0.4-SNAPSHOT.jar target/mongo-project-ejemplo-0.0.4-SNAPSHOT.jar
ENTRYPOINT ["java","-jar","target/mongo-project-ejemplo-0.0.7-SNAPSHOT.jar"]
