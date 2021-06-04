package io.bootique.rabbitmq.client.demo;

import io.bootique.BQCoreModule;
import io.bootique.Bootique;
import io.bootique.di.BQModule;
import io.bootique.di.Binder;
import io.bootique.rabbitmq.client.demo.receiver.ReceiverCommand;
import io.bootique.rabbitmq.client.demo.sender.SenderCommand;

public class Application implements BQModule {
    public static void main(String[] args) {
        Bootique
                .app(args)
                .args("--config=classpath:bootique.yml")
                .module(Application.class)
                .autoLoadModules()
                .exec()
                .exit();
    }

    @Override
    public void configure(Binder binder) {
        BQCoreModule.extend(binder).addCommand(SenderCommand.class).addCommand(ReceiverCommand.class);
    }
}
