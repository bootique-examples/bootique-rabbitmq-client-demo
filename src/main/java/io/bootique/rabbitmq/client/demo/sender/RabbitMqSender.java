package io.bootique.rabbitmq.client.demo.sender;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public interface RabbitMqSender {
    void init() throws IOException, TimeoutException;
}
