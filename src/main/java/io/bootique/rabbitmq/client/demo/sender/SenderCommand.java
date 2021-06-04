package io.bootique.rabbitmq.client.demo.sender;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.ConnectionFactory;
import io.bootique.cli.Cli;
import io.bootique.command.CommandOutcome;
import io.bootique.command.CommandWithMetadata;
import io.bootique.meta.application.CommandMetadata;
import io.bootique.meta.application.OptionMetadata;
import io.bootique.rabbitmq.client.channel.RmqChannelFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Provider;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeoutException;

public class SenderCommand extends CommandWithMetadata {

    static private final Logger LOGGER = LoggerFactory.getLogger(SenderCommand.class);

    private static final String CONNECTION_NAME = "bqConnection";
    private static final String EXCHANGE_NAME = "bqExchange";
    private static final String QUEUE_NAME = "bqQueue";

    private final Provider<RmqChannelFactory> channelFactory;

    @Inject
    public SenderCommand(Provider<RmqChannelFactory> channelFactory) {
        super(CommandMetadata.builder(SenderCommand.class)
                .name("send")
                .description("Send message to RabbitMQ")
                .addOption(OptionMetadata.builder("message", "Message to send").valueOptional().build())
                .build());
        this.channelFactory = channelFactory;
    }

    @Override
    public CommandOutcome run(Cli cli) {

        try {
            if (cli.hasOption("m")) {
                send(cli.optionString("m"));
            } else {
                sendPredefinedMessages();
            }
        } catch (IOException | TimeoutException e) {
            return CommandOutcome.failed(1, "Unable to send message", e);
        }

        return CommandOutcome.succeeded();
    }

    private void send(String message) throws IOException, TimeoutException {
        if (message == null) {
            return;
        }
        try (Channel channel = channelFactory.get().newChannel(CONNECTION_NAME)
                .ensureExchange(EXCHANGE_NAME)
                .ensureQueue(QUEUE_NAME)
                .open()) {
            channel.basicPublish("", QUEUE_NAME, null, message.getBytes(StandardCharsets.UTF_8));
            LOGGER.info("Sent message: " + message);
        }
    }

    private void sendPredefinedMessages() throws IOException, TimeoutException {
        final List<String> messages = Arrays.asList("Revenge?! REVENGE?!", "I will show you REVENGE!", "I am fire.", "I am... DEATH.");

        try (Channel channel = channelFactory.get().newChannel(CONNECTION_NAME)
                .ensureExchange(EXCHANGE_NAME)
                .ensureQueue(QUEUE_NAME)
                .open()) {
            for (final String message : messages) {
                channel.basicPublish("", QUEUE_NAME, null, message.getBytes(StandardCharsets.UTF_8));
            }
        }

        LOGGER.info(" [x] Smaug sent " + messages.size() + " messages\n");
    }
}
