package io.bootique.rabbitmq.client.demo.sender;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import io.bootique.cli.Cli;
import io.bootique.command.Command;
import io.bootique.command.CommandOutcome;
import io.bootique.rabbitmq.client.channel.ChannelFactory;
import io.bootique.rabbitmq.client.connection.ConnectionFactory;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeoutException;

public class SenderCommand implements Command {

    private static final String CONNECTION_NAME = "bqConnection";
    private static final String EXCHANGE_NAME = "bqExchange";
    private static final String QUEUE_NAME = "bqQueue";

    private Provider<ConnectionFactory> connectionFactory;
    private Provider<ChannelFactory> channelFactory;

    @Inject
    public SenderCommand(Provider<ConnectionFactory> connectionFactory, Provider<ChannelFactory> channelFactory) {
        this.connectionFactory = connectionFactory;
        this.channelFactory = channelFactory;
    }

    @Override
    public CommandOutcome run(Cli cli) {

        try {
            send();
        } catch (TimeoutException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return CommandOutcome.succeeded();
    }


    public void send() throws IOException, TimeoutException {

        Connection connection = connectionFactory.get().forName(CONNECTION_NAME);

        Channel channel = channelFactory.get().openChannel(connection, EXCHANGE_NAME, QUEUE_NAME, "");


        final List<String> messages = Arrays.asList("Revenge?! REVENGE?!", "I will show you REVENGE!", "I am fire.", "I am... DEATH.");
        for (final String message : messages) {
            channel.basicPublish("", QUEUE_NAME, null, message.getBytes("UTF-8"));
        }

        System.out.println(" [x] Smaug sent " + messages.size() + " messages\n");

        channel.close();
        connection.close();
    }
}
