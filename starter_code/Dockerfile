FROM tomcat:8.5.83-jdk8-corretto
WORKDIR /opt
ENV PORT 8080
EXPOSE 8080
COPY ./*.jar /opt/ecommerce.jar
ENTRYPOINT exec java $JAVA_OPTS -jar ecommerce.jar