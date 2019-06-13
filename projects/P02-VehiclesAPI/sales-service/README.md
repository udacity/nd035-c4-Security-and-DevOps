# Sales Service

The Sales Service is an application that listen to a JMS Queue and simulates
the process of the Order.

## Features

- JMS Listener
- Custom Message Converter using Jackson Serialization
- Listener error handling via interceptor 

## Instructions

```
$ mvn clean package
```

```
$ java -jar target/sales-service-0.0.1-SNAPSHOT.jar
```

Import it in your favorite IDE as a Maven Project.