package pw.cyberbrain.telegram;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import pw.cyberbrain.telegram.dto.MessageDto;

import java.nio.charset.StandardCharsets;

public class TestSender {
    private final static String QUEUE_NAME = "receive.queue";

    public static void main(String[] argv) throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        try (Connection connection = factory.newConnection();
             Channel channel = connection.createChannel()) {
            channel.queueDeclare(QUEUE_NAME, false, false, false, null);
            //MessageDto messageDto = new MessageDto(571900962L, "test1");
            //channel.basicPublish("", QUEUE_NAME, null, messageDto.toString().getBytes(StandardCharsets.UTF_8));
            //System.out.println(" [x] Sent '" + messageDto + "'");
        }
    }
}
