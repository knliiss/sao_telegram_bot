package dev.knalis.sao_telegram_bot.service;

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

    UserConsumerService userConsumerService;
    UsersApiClient usersApiClient;

    public UserDTO getUser(long chatId) {
        return usersApiClient.getUser(chatId);
    }

    public UserDTO createUser(UserCreateRequest request) {
        return usersApiClient.createUser(request);
    }

    public void setUserLocation(long chatId, short locationId ) {
        usersApiClient.updateUser(chatId, null, locationId, null);
    }

    public void setUserAccountName(long chatId, String accountName) {
        usersApiClient.updateUser(chatId, accountName, null, null);
    }


}
