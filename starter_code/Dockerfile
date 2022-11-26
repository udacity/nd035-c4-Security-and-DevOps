FROM tomcat:8.5.83-jdk8-corretto
WORKDIR /opt
ENV PORT 8080
EXPOSE 8080
COPY target/*.jar /opt/auth-course.jar
ENTRYPOINT exec java $JAVA_OPTS -jar auth-course.jar