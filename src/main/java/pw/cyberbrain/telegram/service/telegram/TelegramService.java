package pw.cyberbrain.telegram.service.telegram;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import pw.cyberbrain.telegram.dto.MessageDto;
import pw.cyberbrain.telegram.service.messaging.IntegrationService;
import pw.cyberbrain.telegram.service.messaging.RabbitClient;

import java.util.ArrayList;
import java.util.List;

@Service
public class TelegramService extends TelegramLongPollingBot implements NotificationService {

    private final Logger logger = LoggerFactory.getLogger(RabbitClient.class);
    private final IntegrationService integrationService;

    @Value("${telegram.bot.token}")
    private String botToken;

    @Value("${telegram.bot.name}")
    private String botName;

    @Autowired
    public TelegramService(IntegrationService integrationService) {
        this.integrationService = integrationService;
    }

    @Override
    public String getBotUsername() {
        return botName;
    }

    @Override
    public String getBotToken() {
        return botToken;
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            MessageDto messageDto = new MessageDto(update.getMessage().getChatId(), new ArrayList<>(List.of(update.getMessage().getText())));
            integrationService.sendForHandling(MessageDto.getMessageFromDto(messageDto));
        }
    }

    @Override
    public boolean sendNotification(MessageDto dto) {
        if (dto != null) {
            SendMessage message = new SendMessage();
            StringBuilder payload = new StringBuilder();
            dto.getPayload().forEach(p -> payload.append(p).append('\n'));
            message.setText(payload.toString());
            try {
                message.setChatId(dto.getChatId());
                Message execute = execute(message);
                if (execute.getChatId().toString().equals(message.getChatId())) {
                    logger.debug("Message " + message + "was successfully sent to chat " + message.getChatId());
                    return true;
                }
            } catch (TelegramApiException e) {
                e.printStackTrace();
                logger.error("Cannot send message to Telegram API!");
                System.exit(1);
            }
        }
        logger.debug("Cannot send message to Telegram API!");
        return false;
    }
}
