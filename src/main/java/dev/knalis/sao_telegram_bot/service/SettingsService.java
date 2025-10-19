package dev.knalis.sao_telegram_bot.service;

import dev.knalis.sao_telegram_bot.client.api.UserSettingsApiClient;
import dev.knalis.sao_telegram_bot.client.dto.SettingsDTO;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class SettingsService {
    
    UserSettingsApiClient userSettingsApiClient;
    
    public SettingsDTO getSettings(Long userId, String category) {
        return userSettingsApiClient.getSettings(userId, category);
    }
    
    public void updateAllSettings(Long userId, SettingsDTO settingsDTO) {
        userSettingsApiClient.updateAllSettings(userId, settingsDTO);
    }
    
    public void toggleSetting(Long userId, String type) {
        userSettingsApiClient.toggleSetting(userId, type);
    }
    
    
    public void updateAllByCategorySettings(long userId, String category, boolean state) {
        var settings = getSettings(userId, category);
        settings.getNotificationSettings().entrySet().forEach(entry -> entry.setValue(state));
        updateAllSettings(userId, settings);
    }
}
