package pw.cyberbrain.telegram.service.messaging;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import pw.cyberbrain.telegram.dto.MessageDto;

import java.util.ArrayList;
import java.util.List;

class RabbitClientTest {

    @Test
    void getPayloadFromMessageDto() {
        ObjectMapper mapper = new ObjectMapper();
        MessageDto dto = new MessageDto();
        List<String> payload = new ArrayList<>();
        payload.add("test1");
        payload.add("test2");
        dto.setChatId(2L);
        dto.setPayload(payload);
        String result = "";
        try {
            result = mapper.writeValueAsString(dto);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        System.out.println(result);
    }
}