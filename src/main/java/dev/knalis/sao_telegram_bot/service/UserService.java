package dev.knalis.sao_telegram_bot.service;

import dev.knalis.sao_telegram_bot.client.api.UserAdditionalAccountsApiClient;
import dev.knalis.sao_telegram_bot.client.api.UsersApiClient;
import dev.knalis.sao_telegram_bot.client.dto.UserCreateRequest;
import dev.knalis.sao_telegram_bot.client.dto.UserDTO;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserService {

    UsersApiClient usersApiClient;
    private final UserAdditionalAccountsApiClient userAdditionalAccountsApiClient;
    
    public UserDTO getUser(long chatId) {
        return usersApiClient.getUser(chatId);
    }

    public UserDTO createUser(UserCreateRequest request) {
        return usersApiClient.createUser(request);
    }

    public void setUserLocation(long chatId, short locationId ) {
        usersApiClient.updateUser(chatId, null, locationId, null);
    }

    public void setUserNickName(long chatId, String nickName) {
        usersApiClient.updateUser(chatId, nickName,  null,null);
    }

    public boolean isNickNameValid(String nickName) {
        return nickName != null && nickName.length() >= 3 && nickName.length() <= 20 && nickName.matches("^[a-zA-Z0-9_]+$");
    }

    public Boolean isAccountNameAvailable(String accountName) {
        return usersApiClient.existsByNickname(accountName);
    }

    // ---- Additional accounts management ----

    public void linkAdditionalAccount(long userId, String username) {
        try {
        userAdditionalAccountsApiClient.linkAdditionalAccount(userId, username);
        } catch (Exception e) {
            throw new IllegalStateException("Не удалось привязать дополнительный аккаунт: " + e.getMessage());
        }
    }

    public void unlinkAdditionalAccount(long userId, String username) {
        userAdditionalAccountsApiClient.unlinkAdditionalAccount(userId, username);
    }

    private String sanitizeNick(String username) {
        return username != null && username.startsWith("@") ? username.substring(1) : username;
    }
}
