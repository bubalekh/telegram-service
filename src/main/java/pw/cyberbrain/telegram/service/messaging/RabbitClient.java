package pw.cyberbrain.telegram.service.messaging;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import pw.cyberbrain.telegram.dto.MessageDto;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.io.IOException;
import java.util.concurrent.TimeoutException;

@Service
public class RabbitClient {
    @Value("${rabbit.queue.receive}")
    private String RECEIVE_QUEUE;
    @Value("${rabbit.queue.transmit}")
    private String TRANSMIT_QUEUE;
    @Value("${rabbit.host}")
    private String HOST;
    private final ConnectionFactory factory = new ConnectionFactory();
    private Connection connection;
    private Channel channel;

    Logger logger = LoggerFactory.getLogger(RabbitClient.class);

    @PostConstruct
    public void initialize() {
        factory.setHost(HOST);
        try {
            connection = factory.newConnection();
            channel = connection.createChannel();
            channel.queueDeclare(RECEIVE_QUEUE, false, false, false, null);
            channel.queueDeclare(TRANSMIT_QUEUE, false, false, false, null);
            logger.info("RabbitMQ client initialization complete");
        } catch (IOException | TimeoutException e) {
            logger.error("RabbitMQ client initialization failed");
            throw new RuntimeException(e);
        }
    }

    public void registerConsumeCallback(DeliverCallback callback) {
        try {
            channel.basicConsume(RECEIVE_QUEUE, true, callback, consumerTag -> {});
            logger.info("RabbitMQ Consume callback has been set up");
        } catch (IOException e) {
            logger.error("RabbitMQ Consume callback set up failed!");
            throw new RuntimeException(e);
        }
    }

    public void send(MessageDto message) {
        try {
            channel.basicPublish("", TRANSMIT_QUEUE, null, message.toString().getBytes());
            logger.info("Message: " + message + " has been sent!");
        } catch (IOException e) {
            logger.error("Message " + message + "was not sent!");
            throw new RuntimeException(e);
        }
    }

    @PreDestroy
    private void terminate() {
        try {
            channel.close();
            connection.close();
            logger.info("RabbitMQ client termination has been completed!");
        } catch (IOException | TimeoutException e) {
            logger.info("Error in RabbitMQ client termination stage!");
            throw new RuntimeException(e);
        }
    }
}
