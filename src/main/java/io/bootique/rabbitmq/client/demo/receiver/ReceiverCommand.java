package io.bootique.rabbitmq.client.demo.receiver;

import com.rabbitmq.client.*;
import io.bootique.cli.Cli;
import io.bootique.command.CommandOutcome;
import io.bootique.command.CommandWithMetadata;
import io.bootique.meta.application.CommandMetadata;
import io.bootique.rabbitmq.client.channel.RmqChannelFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Provider;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class ReceiverCommand extends CommandWithMetadata {

    static private final Logger LOGGER = LoggerFactory.getLogger(ReceiverCommand.class);

    private static final String CONNECTION_NAME = "bqConnection";
    private static final String EXCHANGE_NAME = "bqExchange";
    private static final String QUEUE_NAME = "bqQueue";

    private final Provider<RmqChannelFactory> channelFactory;

    @Inject
    public ReceiverCommand(Provider<RmqChannelFactory> channelFactory) {
        super(CommandMetadata.builder(ReceiverCommand.class)
                .name("receive").description("Receive messages from RabbitMQ").build());
        this.channelFactory = channelFactory;
    }

    @Override
    public CommandOutcome run(Cli cli) {
        try {
            receive();
        } catch (IOException | InterruptedException e) {
            return CommandOutcome.failed(1, "Failed to receive", e);
        }

        return CommandOutcome.succeeded();
    }

    private void receive() throws IOException, InterruptedException {
        Channel channel = channelFactory.get().newChannel(CONNECTION_NAME)
                .ensureExchange(EXCHANGE_NAME)
                .ensureQueue(QUEUE_NAME)
                .open();

        LOGGER.info(" [*] Waiting for messages. To exit press CTRL+C");
        Consumer consumer = new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body)
                    throws IOException {
                String message = new String(body, StandardCharsets.UTF_8);
                LOGGER.info(" [x] Received '" + message + "'");
            }
        };
        channel.basicConsume(QUEUE_NAME, true, consumer);
        Thread.currentThread().join();
    }
}
