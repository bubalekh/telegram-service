package pw.cyberbrain.telegram.service;

import com.rabbitmq.client.DeliverCallback;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pw.cyberbrain.telegram.dto.MessageDto;
import pw.cyberbrain.telegram.service.messaging.RabbitClient;
import pw.cyberbrain.telegram.service.telegram.NotificationService;

import java.nio.charset.StandardCharsets;

@Service
public class ServiceFacade {

    private NotificationService notificationService;

    @Autowired
    public ServiceFacade(NotificationService notificationService, RabbitClient rabbitClient) {
        this.notificationService = notificationService;
        rabbitClient.registerConsumeCallback(deliverCallback);
    }

    DeliverCallback deliverCallback = (consumerTag, delivery) -> {
        String message = new String(delivery.getBody(), StandardCharsets.UTF_8);
        notificationService.notify(MessageDto.getMessageDto(message));
        System.out.println(" [x] Received '" + message + "'");
    };
}
