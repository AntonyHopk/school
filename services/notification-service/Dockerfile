FROM maven:3.9.9-eclipse-temurin-21 AS build
WORKDIR /workspace
COPY . .
RUN mvn -q -DskipTests -pl :notification-service -am clean package
FROM eclipse-temurin:21-jre
WORKDIR /app
COPY --from=build /workspace/services/notification-service/target/*.jar /app/app.jar
EXPOSE 8086
ENTRYPOINT ["java","-jar","/app/app.jar"]