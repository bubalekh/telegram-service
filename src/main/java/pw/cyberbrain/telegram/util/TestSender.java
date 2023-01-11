package pw.cyberbrain.telegram.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import pw.cyberbrain.telegram.dto.MessageDto;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class TestSender {
    private final static String QUEUE_NAME = "test-queue";
    //private final static String QUEUE_NAME = "receive.queue";

    public static void main(String[] argv) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        try (Connection connection = factory.newConnection();
             Channel channel = connection.createChannel()) {
            channel.queueDeclare(QUEUE_NAME, false, false, false, null);
            List<String> payload = new ArrayList<>();
            //payload.add("test1");
            payload.add("Lorem ipsum dolor sit amet, consectetuer adipiscing elit. Aenean commodo ligula eget dolor. Aenean massa. Cum sociis natoque penatibus et magnis dis parturient montes, nascetur ridiculus mus. Donec quam felis, ultricies nec, pellentesque eu, pretium quis, sem. Nulla consequat massa quis enim. Donec pede justo, fringilla vel, aliquet nec, vulputate eget, arcu. In enim justo, rhoncus ut, imperdiet a");
            MessageDto messageDto = new MessageDto(571900962L, payload);
            String message = mapper.writeValueAsString(messageDto);
            channel.basicPublish("", QUEUE_NAME, null, message.getBytes(StandardCharsets.UTF_8));
            System.out.println(" [x] Sent '" + messageDto + "'");
        }
    }
}
