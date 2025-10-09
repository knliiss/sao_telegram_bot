package dev.knalis.sao_telegram_bot.client.api;

import dev.knalis.sao_telegram_bot.client.dto.UserCreateRequest;
import dev.knalis.sao_telegram_bot.client.dto.UserDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name = "usersApi", url = "${api.bot}")
public interface UsersApiClient {
    @GetMapping("/api/v1/users/{id}")
    UserDTO getUser(@PathVariable("id") Long id);

    @PutMapping("/api/v1/users/{id}")
    UserDTO updateUser(@PathVariable("id") Long id,
                       @RequestParam(value = "nickname", required = false) String nickname,
                       @RequestParam(required = false) Short location,
                       @RequestParam(value = "messagePackId", required = false) String messagePackId);

    @DeleteMapping("/api/v1/users/{id}")
    void deleteUser(@PathVariable("id") Long id);

    @GetMapping("/api/v1/users")
    List<UserDTO> getUsers(@RequestParam(value = "setting", required = false) String setting);

    @PostMapping("/api/v1/users")
    UserDTO createUser(@RequestBody UserCreateRequest request);

    @PostMapping("/api/v1/users/{id}/balance/add/{amount}")
    UserDTO addBalance(@PathVariable("id") Long id, @PathVariable("amount") Double amount);

    @PostMapping("/api/v1/users/balance/add-all/{amount}")
    String addBalanceToAll(@PathVariable("amount") Double amount);

    @GetMapping("/api/v1/users/by-nickname/{nickname}")
    UserDTO getUserByNickname(@PathVariable("nickname") String nickname);

    @GetMapping("/exists-by-nickname/{nickname}")
    boolean existsByNickname(@PathVariable String nickname);

}

