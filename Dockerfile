FROM openjdk:11-jre-slim
WORKDIR /app
COPY target/demo-0.1-SNAPSHOT-exec.jar app.jar
EXPOSE 9091
ENTRYPOINT ["java", "-jar", "app.jar"]