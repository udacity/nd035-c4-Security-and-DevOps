FROM openjdk:8
EXPOSE 8080:8081
RUN addgroup -S spring && adduser -S spring -G spring
USER spring:spring
COPY auth-course-0.0.1-SNAPSHOT.jar auth-course-0.0.1-SNAPSHOT.jar ENTRYPOINT ["java","-jar","/auth-course-0.0.1-SNAPSHOT.jar"]