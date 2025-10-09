package dev.knalis.sao_telegram_bot.client.api;

import dev.knalis.sao_telegram_bot.client.dto.MessagePackDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@FeignClient(name = "messagePacksApi", url = "${api.bot}")
public interface MessagePacksApiClient {
    @GetMapping("/api/v1/message-packs")
    List<MessagePackDTO> getAll(@RequestParam(value = "rarity", required = false) String rarity);

    @GetMapping("/api/v1/message-packs/{id}")
    MessagePackDTO getById(@PathVariable("id") String id);

    @GetMapping("/api/v1/message-packs/{id}/messages/{key}")
    String getMessage(@PathVariable("id") String id, @PathVariable("key") String key);

    @GetMapping("/api/v1/message-packs/batch")
    List<MessagePackDTO> getByIds(@RequestParam("ids") List<String> ids);
}

