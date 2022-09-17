FROM openjdk:11-jre-slim

WORKDIR /app

COPY target/*.jar /app/fas-api.jar

EXPOSE 8080

CMD ["java", "-jar", "fas-api.jar"]
