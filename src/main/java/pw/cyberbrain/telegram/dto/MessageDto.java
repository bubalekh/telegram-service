package pw.cyberbrain.telegram.dto;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MessageDto {
    private Long chatId;
    private List<String> payload;

    public static MessageDto getMessageDto(String message) {
        ObjectMapper mapper = new ObjectMapper();
        MessageDto messageDto = new MessageDto();
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
