package dev.knalis.sao_telegram_bot.client.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {
    private short location;
    private long id;
    private Long gangId;
    private long configId;
    private Double balance;
    private String username;
    private String nickname;
    private SubscriptionDTO subscription;
    private List<String> roles;
    private List<String> ownedMessagePacksIds;
    private List<String> additionalAccounts;
    
    public boolean isAdmin() {
        return roles.contains("ADMIN");
    }
}
