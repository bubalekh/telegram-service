package pw.cyberbrain.telegram.service.messaging;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import org.junit.jupiter.api.Test;
import pw.cyberbrain.telegram.dto.MessageDto;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeoutException;

class RabbitClientIntegrationTest {

    @Test
    void sendMessageToRabbitMQ() {
        // If you don't specify RABBIT_QUEUE_RECEIVE evn var, QUEUE_NAME will be "test-queue"
        //private final static String QUEUE_NAME = "test-queue";
        Long chatId = 0L; //Put your telegram chat ID here
        String QUEUE_NAME = "receive.queue";
        ObjectMapper mapper = new ObjectMapper();
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        try (Connection connection = factory.newConnection();
             Channel channel = connection.createChannel()) {
            channel.queueDeclare(QUEUE_NAME, false, false, false, null);
            List<String> payload = new ArrayList<>();
            //payload.add("test1");
            payload.add("Lorem ipsum dolor sit amet, consectetuer adipiscing elit. Aenean commodo ligula eget dolor. Aenean massa. Cum sociis natoque penatibus et magnis dis parturient montes, nascetur ridiculus mus. Donec quam felis, ultricies nec, pellentesque eu, pretium quis, sem. Nulla consequat massa quis enim. Donec pede justo, fringilla vel, aliquet nec, vulputate eget, arcu. In enim justo, rhoncus ut, imperdiet a");
            MessageDto messageDto = new MessageDto(chatId, payload);
            String message = mapper.writeValueAsString(messageDto);
            channel.basicPublish("", QUEUE_NAME, null, message.getBytes(StandardCharsets.UTF_8));
            System.out.println(" [x] Sent '" + messageDto + "'");
        } catch (IOException | TimeoutException e) {
            throw new RuntimeException(e);
        }
    }
}