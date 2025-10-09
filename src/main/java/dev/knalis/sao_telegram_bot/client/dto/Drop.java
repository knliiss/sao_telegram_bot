package dev.knalis.sao_telegram_bot.client.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Drop {
    private String username;
    private String item;
    private LocalDateTime date;
}
