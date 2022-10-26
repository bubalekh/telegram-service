package pw.cyberbrain.telegram.service.telegram;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import pw.cyberbrain.telegram.dto.MessageDto;
import pw.cyberbrain.telegram.service.messaging.RabbitClient;

import java.util.ArrayList;
import java.util.Collections;

@Service
public class TelegramService extends TelegramLongPollingBot implements NotificationService {

    private final RabbitClient client;

    @Value("${telegram.bot.token}")
    private String botToken;

    @Autowired
    public TelegramService(RabbitClient client) {
        this.client = client;
    }

    @Override
    public String getBotUsername() {
        return "callRequestBot";
    }

    @Override
    public String getBotToken() {
        return botToken;
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            client.send(new MessageDto(update.getMessage().getChatId(), new ArrayList<>(Collections.singleton(update.getMessage().getText()))));
        }
    }

    @Override
    public void notify(MessageDto dto) {
        if (dto != null) {
            SendMessage message = new SendMessage();
            StringBuilder payload = new StringBuilder();
            dto.getPayload().forEach(p -> payload.append(p).append('\n'));
            message.setText(payload.toString());
            try {
                message.setChatId(dto.getChatId());
                execute(message);
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        }
    }
}
