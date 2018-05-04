package io.bootique.rabbitmq.client.demo.receiver;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public interface RabbitMqReceiver {
    void init() throws IOException, TimeoutException;
}
