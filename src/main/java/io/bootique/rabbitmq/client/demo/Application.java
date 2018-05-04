package io.bootique.rabbitmq.client.demo;

import com.google.inject.Binder;
import com.google.inject.Module;
import io.bootique.BQRuntime;
import io.bootique.Bootique;
import io.bootique.rabbitmq.client.demo.sender.RabbitMqSender;
import io.bootique.rabbitmq.client.demo.sender.RabbitMqSenderImpl;
import io.bootique.rabbitmq.client.demo.receiver.RabbitMqReceiver;
import io.bootique.rabbitmq.client.demo.receiver.RabbitMqReceiverImpl;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class Application implements Module {
    public static void main(String[] args) {
        BQRuntime runtime = Bootique
                .app(args)
                .args("--config=classpath:bootique.yml")
                .module(Application.class)
                .autoLoadModules()
                .createRuntime();
        try {
            runtime.getInstance(RabbitMqSender.class).init();
            runtime.getInstance(RabbitMqReceiver.class).init();
        } catch (IOException | TimeoutException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void configure(Binder binder) {
        binder.bind(RabbitMqSender.class).to(RabbitMqSenderImpl.class);
        binder.bind(RabbitMqReceiver.class).to(RabbitMqReceiverImpl.class);
    }
}
