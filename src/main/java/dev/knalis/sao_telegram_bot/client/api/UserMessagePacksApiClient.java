package dev.knalis.sao_telegram_bot.client.api;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "userMessagePacksApi", url = "${api.bot}")
public interface UserMessagePacksApiClient {

    @PostMapping("/api/v1/user/message-pack/buy")
    String buyPack(
            @RequestParam("userId") Long userId,
            @RequestParam("packId") String packId
    );
}