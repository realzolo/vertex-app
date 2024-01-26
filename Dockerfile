FROM openjdk:17

WORKDIR /app

COPY vertex-application/target/*.jar app.jar

EXPOSE 10240

ENTRYPOINT ["java", "-jar", "app.jar"]