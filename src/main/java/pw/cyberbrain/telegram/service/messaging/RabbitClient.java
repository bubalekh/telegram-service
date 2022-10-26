package pw.cyberbrain.telegram.service.messaging;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import pw.cyberbrain.telegram.dto.MessageDto;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.io.IOException;
import java.util.concurrent.TimeoutException;

@Service
public class RabbitClient {
    @Value("${notification.receive.queue}")
    private String RECEIVE_QUEUE;
    @Value("${notification.transmit.queue}")
    private String TRANSMIT_QUEUE;
    @Value("${notification.host}")
    private String HOST;
    private final ConnectionFactory factory = new ConnectionFactory();
    private Connection connection;
    private Channel channel;

    @PostConstruct
    public void initialize() throws TimeoutException, IOException {
        factory.setHost(HOST);
        connection = factory.newConnection();
        channel = connection.createChannel();

        channel.queueDeclare(RECEIVE_QUEUE, false, false, false, null);
        channel.queueDeclare(TRANSMIT_QUEUE, false, false, false, null);
        System.out.println(" [*] Message Receiver has been started!");
    }

    public void registerConsumeCallback(DeliverCallback callback) {
        try {
            channel.basicConsume(RECEIVE_QUEUE, true, callback, consumerTag -> {});
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void send(MessageDto message) {
        try {
            channel.basicPublish("", TRANSMIT_QUEUE, null, message.toString().getBytes());
            System.out.println(" [x] Sent '" + message + "'");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
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
