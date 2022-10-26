package pw.cyberbrain.telegram.service.telegram;

import pw.cyberbrain.telegram.dto.MessageDto;

public interface NotificationService {
    void sendNotification(MessageDto messageDto);
}
