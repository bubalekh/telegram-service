package pw.cyberbrain.telegram.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import pw.cyberbrain.telegram.dto.MessageDto;

public class MessageUtils {
    public static MessageDto getMessageDto(String message) {
        ObjectMapper mapper = new ObjectMapper();
        MessageDto messageDto = new MessageDto(null, null);
        try {
            messageDto = mapper.readValue(message, MessageDto.class);
        } catch (JsonProcessingException e) {
            System.out.println("Incorrect message!");
        }
        return messageDto;
    }
    public static String getMessageFromDto(MessageDto messageDto) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.writeValueAsString(messageDto);
        } catch (JsonProcessingException e) {
            System.out.println("Incorrect message!");
        }
        return "null";
    }
}
