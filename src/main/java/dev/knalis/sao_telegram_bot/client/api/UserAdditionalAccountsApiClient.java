package dev.knalis.sao_telegram_bot.client.api;

import dev.knalis.sao_telegram_bot.client.dto.UserDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name = "userAdditionalAccountsApiClient", url = "${api.bot}")
public interface UserAdditionalAccountsApiClient {
    String ADDITIONAL_ACCOUNTS_ENDPOINT = "/api/v1/users/{userId}/additional-accounts";
    
    // ----------------- LINK ACCOUNT -----------------
    
    @PostMapping(ADDITIONAL_ACCOUNTS_ENDPOINT + "/link")
    List<String> linkAdditionalAccount(@PathVariable long userId, @RequestBody String accountName);
    
    // ----------------- UNLINK ACCOUNT -----------------
    
    @PostMapping(ADDITIONAL_ACCOUNTS_ENDPOINT + "/unlink")
    List<String> unlinkAdditionalAccount(@PathVariable long userId, @RequestBody String accountName);
    
    // ----------------- GET ACCOUNTS BY NAME -----------------
    
    @GetMapping(ADDITIONAL_ACCOUNTS_ENDPOINT + "/by-name")
    List<UserDTO> getUsersByAdditionalAccount(@RequestBody String accountName);
    
    // ----------------- GET ACCOUNTS BY USERID -----------------
    
    @GetMapping(ADDITIONAL_ACCOUNTS_ENDPOINT)
    List<String> getAdditionalAccounts(@PathVariable long userId);
}
