package dev.knalis.sao_telegram_bot.service;

import dev.knalis.sao_telegram_bot.client.api.UserConfigsApiClient;
import dev.knalis.sao_telegram_bot.client.dto.ConfigDTO;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ConfigService {
    
    UserConfigsApiClient userConfigsApiClient;
    
    public void activateConfig(long userId, long configId) {
        userConfigsApiClient.setActiveConfig(userId, configId);
    }
    
    public List<ConfigDTO> getUserConfigs(long userId, boolean active) {
        return userConfigsApiClient.getUserConfigs(userId, active);
    }
}
