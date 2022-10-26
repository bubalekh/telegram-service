package pw.cyberbrain.telegram.service.messaging;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.DeliverCallback;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.Channel;
import pw.cyberbrain.telegram.dto.MessageDto;
import pw.cyberbrain.telegram.service.telegram.NotificationService;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeoutException;

@Service
public class RabbitClient {
    private final NotificationService service;
    @Value("${notification.receive.queue}")
    private String RECEIVE_QUEUE;
    @Value("${notification.transmit.queue}")
    private String TRANSMIT_QUEUE;
    @Value("${notification.host}")
    private String HOST;
    private final ConnectionFactory factory = new ConnectionFactory();
    private Connection connection;
    private Channel channel;

    @Autowired
    public RabbitClient(@Lazy NotificationService service) {
        this.service = service;
    }

    @PostConstruct
    public void initialize() throws TimeoutException, IOException {
        factory.setHost(HOST);
        connection = factory.newConnection();
        channel = connection.createChannel();

        channel.queueDeclare(RECEIVE_QUEUE, false, false, false, null);
        channel.queueDeclare(TRANSMIT_QUEUE, false, false, false, null);
        System.out.println(" [*] Message Receiver has been started!");

        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            String message = new String(delivery.getBody(), StandardCharsets.UTF_8);
            service.notify(getMessageDto(message));
            System.out.println(" [x] Received '" + message + "'");
        };

        channel.basicConsume(RECEIVE_QUEUE, true, deliverCallback, consumerTag -> {
        });
    }

    public void send(MessageDto message) {
        try {
            channel.basicPublish("", TRANSMIT_QUEUE, null, message.toString().getBytes());
            System.out.println(" [x] Sent '" + message + "'");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private MessageDto getMessageDto(String message) {
        ObjectMapper mapper = new ObjectMapper();
        MessageDto messageDto = new MessageDto();
        try {
            messageDto = mapper.readValue(message, MessageDto.class);
        } catch (JsonProcessingException e) {
            System.out.println("Incorrect message!");
        }
        return messageDto;
    }

    public String getPayloadFromMessageDto(MessageDto dto) {
        return null;
    }

    @PreDestroy
    private void terminate() {
        try {
            channel.close();
            connection.close();
        } catch (IOException | TimeoutException e) {
            throw new RuntimeException(e);
        }
    }
}
