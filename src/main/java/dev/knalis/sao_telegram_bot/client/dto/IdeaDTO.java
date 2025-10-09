package dev.knalis.sao_telegram_bot.client.dto;

import dev.knalis.sao_telegram_bot.model.IdeaStatus;
import lombok.Data;

import java.time.Instant;

@Data
public class IdeaDTO {
    private String id;
    private String content;
    private Long authorId;
    private IdeaStatus status;
    private Instant created;
}
