package pw.cyberbrain.telegram.dto;

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

    /*@Override
    public String toString() {
        return "{\n" +
                "\"chatId\": " + chatId + ",\n" +
                "\"payload\": \"" + payload  + "\"\n" +
                "}\n";
    }*/
}
