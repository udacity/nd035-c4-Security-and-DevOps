#stage 1
#Start with a base image containing Java runtime
FROM openjdk:11-slim as build

WORKDIR build

# The application's jar file
ARG JAR_FILE=target/*.jar

# Add the application's jar to the container
COPY ${JAR_FILE} app.jar

#unpackage jar file
RUN java -Djarmode=layertools -jar app.jar extract

#stage 2
#Same Java runtime
FROM openjdk:11-slim

WORKDIR build

COPY --from=build build/dependencies/ build/snapshot-dependencies/ build/spring-boot-loader/ build/application/ ./

#execute the application
ENTRYPOINT ["java", "org.springframework.boot.loader.JarLauncher"]