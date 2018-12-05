package io.bootique.rabbitmq.client.demo.receiver;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.Consumer;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;
import io.bootique.cli.Cli;
import io.bootique.command.CommandOutcome;
import io.bootique.command.CommandWithMetadata;
import io.bootique.meta.application.CommandMetadata;
import io.bootique.rabbitmq.client.channel.ChannelFactory;
import io.bootique.rabbitmq.client.connection.ConnectionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class ReceiverCommand extends CommandWithMetadata {

    static private final Logger LOGGER = LoggerFactory.getLogger(ReceiverCommand.class);

    private static final String CONNECTION_NAME = "bqConnection";
    private static final String EXCHANGE_NAME = "bqExchange";
    private static final String QUEUE_NAME = "bqQueue";

    private Provider<ConnectionFactory> connectionFactory;
    private Provider<ChannelFactory> channelFactory;

    @Inject
    public ReceiverCommand(Provider<ConnectionFactory> connectionFactory, Provider<ChannelFactory> channelFactory) {
        super(CommandMetadata.builder(ReceiverCommand.class)
                .name("receive").description("Receive messages from RabbitMQ").build());
        this.connectionFactory = connectionFactory;
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
        Connection connection = connectionFactory.get().forName(CONNECTION_NAME);
        Channel channel = channelFactory.get().openChannel(connection, EXCHANGE_NAME, QUEUE_NAME, "");

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
