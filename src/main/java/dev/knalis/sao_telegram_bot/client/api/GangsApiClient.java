package dev.knalis.sao_telegram_bot.client.api;

import dev.knalis.sao_telegram_bot.client.dto.GangDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@FeignClient(name = "gangsApi", url = "${api.bot}")
public interface GangsApiClient {
    @GetMapping("/api/v1/gangs")
    List<GangDTO> getAllGangs();

    @PostMapping("/api/v1/gangs")
    GangDTO createGang(@RequestParam("userId") Long userId, @RequestParam("name") String name);

    @GetMapping("/api/v1/gangs/{gangId}")
    GangDTO getGang(@PathVariable("gangId") Long gangId);

    @PostMapping("/api/v1/gangs/{userId}/transfer")
    void transferOwnership(@PathVariable("userId") Long userId, @RequestParam("newOwnerId") Long newOwnerId);

    @PostMapping("/api/v1/gangs/{gangId}/members")
    void addMember(@PathVariable("gangId") Long gangId, @RequestParam("actorId") Long actorId, @RequestParam("targetId") Long targetId);

    @DeleteMapping("/api/v1/gangs/{gangId}/members/{userId}")
    void removeMember(@PathVariable("gangId") Long gangId, @PathVariable("userId") Long userId, @RequestParam("actorId") Long actorId);

    @PostMapping("/api/v1/gangs/{gangId}/join")
    void joinGang(@PathVariable("gangId") Long gangId, @RequestParam("userId") Long userId);

    @PostMapping("/api/v1/gangs/leave")
    void leaveGang(@RequestParam("userId") Long userId);
}

