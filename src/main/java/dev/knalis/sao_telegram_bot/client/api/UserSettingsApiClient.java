package dev.knalis.sao_telegram_bot.client.api;

import dev.knalis.sao_telegram_bot.client.dto.SettingsDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "userSettingsApi", url = "${api.bot}")
public interface UserSettingsApiClient {
    @GetMapping("/api/v1/users/{userId}/settings")
    SettingsDTO getSettings(@PathVariable("userId") Long userId, @RequestParam String category);

    @PutMapping("/api/v1/users/{userId}/settings")
    void updateAllSettings(@PathVariable("userId") Long userId, @RequestBody SettingsDTO settingsDTO);

    @PatchMapping("/api/v1/users/{userId}/settings/{type}")
    void toggleSetting(@PathVariable("userId") Long userId, @PathVariable("type") String type);
}

