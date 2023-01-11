package pw.cyberbrain.telegram.probes;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;
import pw.cyberbrain.telegram.service.telegram.TelegramService;

@Component
public class TelegramState implements HealthIndicator {
        @Autowired
    TelegramService telegramService;
    @Override
    public Health health() {
        if (telegramService.isTelegramReady()) {
            return Health.up().build();
        }
        return Health.down().build();
    }
}
