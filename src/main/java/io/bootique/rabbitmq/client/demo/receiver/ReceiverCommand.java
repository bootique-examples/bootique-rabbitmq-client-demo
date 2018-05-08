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
import io.bootique.command.Command;
import io.bootique.command.CommandOutcome;
import io.bootique.rabbitmq.client.channel.ChannelFactory;
import io.bootique.rabbitmq.client.connection.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class ReceiverCommand implements Command {

    private static final String CONNECTION_NAME = "bqConnection";
    private static final String EXCHANGE_NAME = "bqExchange";
    private static final String QUEUE_NAME = "bqQueue";

    private Provider<ConnectionFactory> connectionFactory;
    private Provider<ChannelFactory> channelFactory;

    @Inject
    public ReceiverCommand(Provider<ConnectionFactory> connectionFactory, Provider<ChannelFactory> channelFactory) {
        this.connectionFactory = connectionFactory;
        this.channelFactory = channelFactory;
    }

    public void receive() throws IOException {
        Connection connection = connectionFactory.get().forName(CONNECTION_NAME);
        Channel channel = channelFactory.get().openChannel(connection, EXCHANGE_NAME, QUEUE_NAME, "");

        System.out.println(" [*] Waiting for messages. To exit press CTRL+C");
        Consumer consumer = new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body)
                    throws IOException {
                String message = new String(body, "UTF-8");
                System.out.println(" [x] Received '" + message + "'");
            }
        };
        channel.basicConsume(QUEUE_NAME, true, consumer);
    }


    @Override
    public CommandOutcome run(Cli cli) {
        try {
            receive();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return CommandOutcome.succeeded();
    }
}
