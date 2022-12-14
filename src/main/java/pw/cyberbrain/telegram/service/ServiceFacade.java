package pw.cyberbrain.telegram.service;

import com.rabbitmq.client.DeliverCallback;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pw.cyberbrain.telegram.dto.MessageDto;
import pw.cyberbrain.telegram.service.messaging.IntegrationService;
import pw.cyberbrain.telegram.service.messaging.RabbitClient;
import pw.cyberbrain.telegram.service.telegram.NotificationService;
import pw.cyberbrain.telegram.util.MessageUtils;

import java.nio.charset.StandardCharsets;


@Service
public class ServiceFacade {
    private final Logger logger = LoggerFactory.getLogger(RabbitClient.class);

    @Autowired
    public ServiceFacade(NotificationService notificationService, IntegrationService integrationService) {
        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            String message = new String(delivery.getBody(), StandardCharsets.UTF_8);
            integrationService.setMessageAck(delivery, notificationService.sendNotification(MessageUtils.getMessageDto(message)));
            logger.debug("Message " + message + " has been received!");
        };
        integrationService.setConsumingCallback(deliverCallback);
    }
}
