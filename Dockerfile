FROM eclipse-temurin:17.0.17_10-jdk AS build
WORKDIR /app
COPY pom.xml .
RUN mvn -q -DskipTests dependency:go-offline
COPY src ./src
RUN mvn -q -DskipTests package

FROM eclipse-temurin:17.0.17_10-jre
WORKDIR /app
COPY --from=build /app/target/taskflow-api-*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","/app/app.jar"]
