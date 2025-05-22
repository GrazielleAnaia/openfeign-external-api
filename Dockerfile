FROM openjdk:17-jdk-alpine

WORKDIR /app

COPY build/libs/openfeign-external-api-0.0.1-SNAPSHOT.jar /app/openfeign-external-api.jar

EXPOSE 8080

CMD ["java", "-jar", "/app/openfeign-external-api.jar"]