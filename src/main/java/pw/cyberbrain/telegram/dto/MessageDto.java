package pw.cyberbrain.telegram.dto;

import java.util.List;

public record MessageDto(Long chatId, List<String> payload) {}
