package dev.knalis.sao_telegram_bot.service;

import dev.knalis.sao_telegram_bot.client.api.IdeaApiClient;
import dev.knalis.sao_telegram_bot.client.dto.IdeaCreateRequest;
import dev.knalis.sao_telegram_bot.client.dto.IdeaDTO;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class IdeaService {
    
    IdeaApiClient ideaApiClient;
    
    public List<IdeaDTO> getIdeas() {
        return ideaApiClient.getIdea(null, null);
    }
    
    public List<IdeaDTO> getIdeasByUserId(long userId) {
        return ideaApiClient.getIdea(userId, null);
    }
    
    public List<IdeaDTO> getIdeasByStatus(String status) {
        return ideaApiClient.getIdea(null, status);
    }
    
    public IdeaDTO createIdea(IdeaCreateRequest request) {
        return ideaApiClient.postIdea(request);
    }
    
    public void updateIdeaStatus(String id, String status) {
        ideaApiClient.updateIdeaStatus(id, status);
    }
    
    public void deleteIdea(String id) {
        ideaApiClient.deleteIdea(id);
    }
    
    public List<IdeaDTO> getIdeasPage(int page, int pageSize) {
        List<IdeaDTO> all = getIdeas();
        int from = page * pageSize;
        int to = Math.min(from + pageSize, all.size());
        if (from >= all.size()) return List.of();
        return all.subList(from, to);
    }

    public List<IdeaDTO> getAllIdeasPage(int page, int pageSize) {
        // Для админов можно возвращать все идеи, либо с фильтрацией по статусу
        return getIdeasPage(page, pageSize);
    }
    
    
}
