FROM maven:3.9.9-eclipse-temurin-21 AS build
WORKDIR /workspace

COPY . .

# если artifactId = api-gateway
RUN mvn -DskipTests -pl :api-gateway -am clean package

FROM eclipse-temurin:21-jre
WORKDIR /app

COPY --from=build /workspace/services/api-gateway/target/*.jar /app/app.jar

EXPOSE 8080
ENTRYPOINT ["java","-jar","/app/app.jar"]