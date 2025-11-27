FROM eclipse-temurin:25.0.1_8-jdk AS build
WORKDIR /app
COPY pom.xml .
RUN mvn -q -DskipTests dependency:go-offline
COPY src ./src
RUN mvn -q -DskipTests package

FROM eclipse-temurin:25.0.1_8-jre
WORKDIR /app
COPY --from=build /app/target/taskflow-api-*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","/app/app.jar"]
