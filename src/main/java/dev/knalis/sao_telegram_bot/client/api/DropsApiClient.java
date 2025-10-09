package dev.knalis.sao_telegram_bot.client.api;

import dev.knalis.sao_telegram_bot.client.dto.Drop;
import dev.knalis.sao_telegram_bot.client.dto.PlayerDrops;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@FeignClient(name = "dropsApi", url = "${api.bot}")
public interface DropsApiClient {
    @GetMapping("/api/v1/drops")
    List<Drop> getAllDrops(@RequestParam(value = "username", required = false) String username,
                          @RequestParam(value = "itemName", required = false) String itemName);

    @PostMapping("/api/v1/drops")
    PlayerDrops addDrop(@RequestBody Drop drop);
}

