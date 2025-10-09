package dev.knalis.sao_telegram_bot.client.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ConfigDTO {
    private Long id;
    private String name;
    private SettingsDTO settings;
}
