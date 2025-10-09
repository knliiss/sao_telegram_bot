package dev.knalis.sao_telegram_bot.client.api;

import dev.knalis.sao_telegram_bot.client.dto.MessagePackDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name = "userMessagePacksApi", url = "${api.bot}")
public interface UserMessagePacksApiClient {

    // ---- Купить message pack ----
    @PostMapping("/api/v1/user/{userId}/message-pack/buy")
    String buyPack(
            @PathVariable("userId") Long userId,
            @RequestParam("packId") String packId
    );

    // ---- Получить message packs пользователя ----
    @GetMapping("/api/v1/user/{userId}/message-pack")
    List<MessagePackDTO> getUserMessagePacks(
            @PathVariable("userId") Long userId,
            @RequestParam(value = "active", defaultValue = "true") boolean active
    );
}