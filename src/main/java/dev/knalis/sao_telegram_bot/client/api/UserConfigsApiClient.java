package dev.knalis.sao_telegram_bot.client.api;

import dev.knalis.sao_telegram_bot.client.dto.ConfigDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@FeignClient(name = "userConfigsApi", url = "${api.bot}")
public interface UserConfigsApiClient {
    @GetMapping("/api/v1/users/{userId}/config")
    List<ConfigDTO> getUserConfigs(@PathVariable("userId") Long userId, @RequestParam(value = "active", required = false) Boolean active);

    @PostMapping("/api/v1/users/{userId}/config")
    ConfigDTO createUserConfig(@PathVariable("userId") Long userId, @RequestBody String name);

    @PatchMapping("/api/v1/users/{userId}/config/activate/{configId}")
    void setActiveConfig(@PathVariable("userId") Long userId, @PathVariable("configId") Long configId);

    @DeleteMapping("/api/v1/users/{userId}/config/{configId}")
    void deleteUserConfig(@PathVariable("userId") Long userId, @PathVariable("configId") Long configId);
}

