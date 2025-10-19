package dev.knalis.sao_telegram_bot.client.api;

import dev.knalis.sao_telegram_bot.client.dto.IdeaCreateRequest;
import dev.knalis.sao_telegram_bot.client.dto.IdeaDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name = "ideaApi", url = "${api.bot}")
public interface    IdeaApiClient {

    @PostMapping("/api/v1/ideas")
    IdeaDTO postIdea(@RequestBody IdeaCreateRequest createRequest);

    @GetMapping("/api/v1/ideas")
    List<IdeaDTO> getIdea(
            @RequestParam(required = false) Long authorId,
                          @RequestParam(required = false) String status
    );


    @PatchMapping("/api/v1/ideas/{id}/status")
    void updateIdeaStatus(
            @PathVariable String id,
            @RequestParam String status);

    @DeleteMapping("/api/v1/ideas/{id}")
    void deleteIdea(@PathVariable String id);
}

