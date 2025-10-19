package dev.knalis.sao_telegram_bot.service;

import dev.knalis.sao_telegram_bot.client.api.GangsApiClient;
import dev.knalis.sao_telegram_bot.client.dto.GangDTO;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class GangService {
    
    GangsApiClient gangsApiClient;
    private final UserService userService;
    
    public List<GangDTO> getGangs() {
        return gangsApiClient.getAllGangs();
    }
    
    public GangDTO getGang(long gangId) {
        return gangsApiClient.getGang(gangId);
    }
    
    public void joinGang(long userId, long gangId) {
        gangsApiClient.joinGang(userId, gangId);
    }
    
    public void leaveGang(long userId) {
        gangsApiClient.leaveGang(userId);
    }
    
    public GangDTO createGang(long userId, String gangName) {
        return gangsApiClient.createGang(userId, gangName);
    }
    
    public void transferOwnership(long userId, long newOwnerId) {
        gangsApiClient.transferOwnership(userId, newOwnerId);
    }
    
    public void addMember(long actorId, long targetId, long gangId) {
        gangsApiClient.addMember(gangId, actorId, targetId);
    }
    
    public void removeMember(long actorId, long targetId, long gangId) {
        gangsApiClient.removeMember(gangId, targetId, actorId);
    }
    
    public GangDTO getGangByUserId(long userId) {
        var user = userService.getUser(userId);
        var gangId = user.getGangId();
        if (gangId > 1) {
            return getGang(gangId);
        }
        throw new NullPointerException("User is not in a gang");
    }
    
    public boolean isGangNameAvailable(String input) {
        var gangs = getGangs();
        return gangs.stream().noneMatch(gang -> gang.getName().equalsIgnoreCase(input));
    }
    
    public void kickMember(long userId, long gangId, long targetId) {
        var gang = getGang(gangId);
        if (gang.getOwner().getId() != userId) {
            throw new IllegalStateException("Only the gang owner can kick members");
        }
        if (gang.getOwner().getId() == targetId) {
            throw new IllegalStateException("You cannot kick the gang owner");
        }
        removeMember(userId, targetId, gangId);
    }
}
