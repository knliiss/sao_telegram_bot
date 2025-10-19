package dev.knalis.sao_telegram_bot.service;

import dev.knalis.sao_telegram_bot.client.api.MessagePacksApiClient;
import dev.knalis.sao_telegram_bot.client.api.UserMessagePacksApiClient;
import dev.knalis.sao_telegram_bot.client.dto.MessagePackDTO;
import dev.knalis.sao_telegram_bot.model.Rarity;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class MessagePackService {
    
    MessagePacksApiClient messagePacksApiClient;
    UserMessagePacksApiClient userMessagePacksApiClient;
    
    public List<MessagePackDTO> getAllMessagePacks() {
        return messagePacksApiClient.getAll(null);
    }
    
    public List<MessagePackDTO> getPacksByRarity(Rarity rarity) {
        return messagePacksApiClient.getAll(String.valueOf(rarity));
    }
    
    public MessagePackDTO getPackById(String id) {
        return messagePacksApiClient.getById(id);
    }
    
    public void buyPack(long userId, String packId) {
        userMessagePacksApiClient.buyPack(userId, packId);
    }
}
