package io.bootique.rabbitmq.client.demo;

import com.google.inject.Binder;
import com.google.inject.Module;
import io.bootique.BQCoreModule;
import io.bootique.Bootique;
import io.bootique.rabbitmq.client.demo.receiver.ReceiverCommand;
import io.bootique.rabbitmq.client.demo.sender.SenderCommand;

public class Application implements Module {
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
