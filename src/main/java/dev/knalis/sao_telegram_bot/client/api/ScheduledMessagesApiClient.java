package dev.knalis.sao_telegram_bot.client.api;

import dev.knalis.sao_telegram_bot.client.dto.ScheduledMessage;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@FeignClient(name = "scheduledMessagesApi", url = "${api.bot}")
public interface ScheduledMessagesApiClient {
    @GetMapping("/api/v1/scheduled-messages")
    List<ScheduledMessage> getScheduledMessages(@RequestParam(value = "userId", required = false) Long userId);

    @PostMapping("/api/v1/scheduled-messages")
    ScheduledMessage createScheduledMessage(@RequestBody ScheduledMessage scheduledMessage);

    @PutMapping("/api/v1/scheduled-messages/{id}")
    ScheduledMessage updateScheduledMessage(@PathVariable("id") String id,
                                            @RequestParam(value = "content", required = false) String content,
                                            @RequestParam(value = "scheduledTime", required = false) Long scheduledTime);

    @DeleteMapping("/api/v1/scheduled-messages/{id}")
    void deleteScheduledMessage(@PathVariable("id") String id);
}

