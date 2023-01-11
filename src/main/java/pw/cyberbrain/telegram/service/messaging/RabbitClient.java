package pw.cyberbrain.telegram.service.messaging;

import com.rabbitmq.client.*;
import org.aopalliance.reflect.Class;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.io.IOException;
import java.util.concurrent.TimeoutException;

@Service
public class RabbitClient implements IntegrationService {
    @Value("${rabbit.queue.receive}")
    private String RECEIVE_QUEUE;
    @Value("${rabbit.queue.transmit}")
    private String TRANSMIT_QUEUE;
    @Value("${rabbit.host}")
    private String HOST;
    @Value("${rabbit.username}")
    private String USERNAME;
    @Value("${rabbit.password}")
    private String PASSWORD;
    private final ConnectionFactory factory = new ConnectionFactory();
    private Connection connection;
    private Channel channel;

    Logger logger = LoggerFactory.getLogger(RabbitClient.class);

    @PostConstruct
    public void initialize() {
        factory.setHost(HOST);
        factory.setUsername(USERNAME);
        factory.setPassword(PASSWORD);
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

    @Override
    public void setConsumingCallback(Object callback) {
        try {
            if (callback instanceof DeliverCallback deliverCallback) {
                channel.basicConsume(RECEIVE_QUEUE, false, deliverCallback, consumerTag -> {});
                logger.info("RabbitMQ Consume callback has been set up");
            }
            else System.out.println("RabbitMQ callback object has wrong type!");
        } catch (IOException e) {
            logger.error("RabbitMQ Consume callback set up failed!");
            throw new RuntimeException(e);
        }
    }

    @Override
    public void sendForHandling(String message) {
        try {
            channel.basicPublish("", TRANSMIT_QUEUE, null, message.getBytes());
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

    @Override
    public void setMessageAck(Object message, boolean isHandled) {
        try {
            if (message instanceof Delivery delivery) {
                if (isHandled) {
                    channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
                } else {
                    channel.basicNack(delivery.getEnvelope().getDeliveryTag(), false, true);
                }
            }
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
