# bootique-rabbitmq-client-demo

Simple [Bootique](http://bootique.io) app demonstrating the Bootique rabbitmq  module.


# Prerequisites
* Java 1.8 or newer.
* Apache Maven.

# Install rabbitmq
First you should install the rabbitmq docker image with the command:

```
docker run -d --name rbbt -e RABBITMQ_DEFAULT_USER=guest -e RABBITMQ_DEFAULT_PASS=guest -p 55672:5672/tcp rabbitmq:latest
```
 Now you will have acces to rabbitmq with url - localhost:55672


# Build the demo

```
git clone https://github.com/bootique-examples/bootique-rabbitmq-client-demo.git
cd bootique-rabbitmq-client-demo
mvn package
```

# Run the demo

Send messages to rabbitmq channel with -s (--sender):
```
java -jar target/bootique-rebbitmq-demo-1.0-SNAPSHOT.jar -s
```

You will see the result

```
 [x] Smaug sent 4 messages
```

Receive messages from rabbitmq channel with -r (--receiver):
```
java -jar target/bootique-rebbitmq-demo-1.0-SNAPSHOT.jar -r
```

You will see the result
```
 [*] Waiting for messages. To exit press CTRL+C
 [x] Received 'Revenge?! REVENGE?!'
 [x] Received 'I will show you REVENGE!'
 [x] Received 'I am fire.'
 [x] Received 'I am... DEATH.'
```