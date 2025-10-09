package dev.knalis.sao_telegram_bot.client.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MessagePackDTO {
    private String id;
    private String name;
    private String emoji;
    private Map<String, String> messages;
    private Double cost;
    private String rarity;
}
