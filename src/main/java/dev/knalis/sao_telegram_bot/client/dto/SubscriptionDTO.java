package dev.knalis.sao_telegram_bot.client.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SubscriptionDTO {
    private String plan;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
}
