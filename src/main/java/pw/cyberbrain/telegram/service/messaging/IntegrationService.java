package pw.cyberbrain.telegram.service.messaging;

public interface IntegrationService {
    void sendForHandling(String message);
    void setConsumingCallback(Object callback);
}
