# Pricing Service

The Pricing Service is a SOAP WebService that simulates a backend that
would store and retrieve the price of a vehicle given a vehicle id as
input.

The WSDL of the service can be accessed at http://localhost:8082/services/price?wsdl

## Features

- SOAP WebService with an implementation-first approach
- JAX-WS/CXF WebService integrated with Spring Boot

## Instructions

To run this service you execute:

```
$ mvn clean package
```

```
$ java -jar target/pricing-service-0.0.1-SNAPSHOT.jar
```

It can also be imported in your IDE as a Maven project.