[![verify](https://github.com/bootique-examples/bootique-rabbitmq-client-demo/actions/workflows/verify.yml/badge.svg)](https://github.com/bootique-examples/bootique-rabbitmq-client-demo/actions/workflows/verify.yml)
# bootique-rabbitmq-client-demo

Simple [Bootique](http://bootique.io) app demonstrating the Bootique rabbitmq  module.

You can find different versions of framework in use at
* [1.x](https://github.com/bootique-examples/bootique-rabbitmq-client-demo/tree/1.x)
* [2.x](https://github.com/bootique-examples/bootique-rabbitmq-client-demo/tree/2.x)

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
# Available commands

To see all available commands run demo without arguments:

```
java -jar target/bootique-rabbitmq-demo-1.0-SNAPSHOT.jar
```

You will see help:

```
NAME
      bootique-rabbitmq-demo-1.0-SNAPSHOT.jar

OPTIONS
      -c yaml_location, --config=yaml_location
           Specifies YAML config location, which can be a file path or a URL.

      -h, --help
           Prints this message.

      -H, --help-config
           Prints information about application modules and their configuration options.

      -m [val], --message[=val]
           Message to send

      -r, --receive
           Receive messages from RabbitMQ

      -s, --send
           Send message to RabbitMQ
```

# Run the demo

Send messages to rabbitmq channel with `--send` (`-s`) command:

```
java -jar target/bootique-rabbitmq-demo-1.0-SNAPSHOT.jar -s -m "Hello World"
```

You will see the result

```
...  Sent message: hello world
```

Receive messages from rabbitmq channel with `--receive` (`-r`) command:
```
java -jar target/bootique-rabbitmq-demo-1.0-SNAPSHOT.jar -r
```

You will see the result
```
...  [*] Waiting for messages. To exit press CTRL+C
...  [x] Received 'Hello World'
```