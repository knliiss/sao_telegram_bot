package dev.knalis.sao_telegram_bot.client.dto;

import lombok.Data;

@Data
public class IdeaCreateRequest {
    private String content;
    private Long authorId;
}
