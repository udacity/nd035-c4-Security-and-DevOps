# Orders API

The Orders API is a REST application that implements an endpoint for 
placing a Sales Order for a Vehicle as well as an endpoint for canceling 
an Order.

The main purpose of this application is to communicate via JMS with the
Sales Service backend.

### Place an Order

`POST` `/orders`

```json
{
  "vehicleId": 10,
  "customerId": "123"
}
``` 

### Cancel an Order

`DELETE` `/orders/{id}`

## Features

- REST 
- Hateoas
- JMS Producer
- Custom Message Converter using Jackson Serialization
- Exception with HTTP status code

## Instructions

To run this application you can execute

```
$ mvn clean package
````
and then

```
$ java -jar target/orders-api-0.0.1-SNAPSHOT.jar
```

Alternatively, you can use your preferred IDE and import the code
as a Maven project.

## Message Broker

To run the Orders API and be able to send messages to the Message Broker via JMS
you'll need to download and start the broker. For this example we are using
Apache ActiveMQ.

1) Download ActiveMQ 5+: https://activemq.apache.org/components/classic/download/ for your 
platform and unzip it
2) If on Linux or MacOS you can do `tar -xzvf apache-activemq-5.15.9-bin.tar.gz`
3) cd apache-activemq-5.15.9/bin
4) ./activemq start
5) or ./activemq stop