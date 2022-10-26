package pw.cyberbrain.telegram.dto;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class MessageDtoTest {

    @Test
    void getMessageDto() {
        String reference = "{\"chatId\": 571900962, \"payload\": [\"test1\", \"test2\"]}";
        MessageDto testMessage = MessageDto.getMessageDto(reference);
        List<String> payload = new LinkedList<>();
        payload.add("test1");
        payload.add("test2");
        MessageDto referenceMessage = new MessageDto(571900962L, payload);
        assertEquals(testMessage, referenceMessage);
    }
}