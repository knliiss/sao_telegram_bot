package dev.knalis.sao_telegram_bot.client.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GangMemberDTO {
    private Long id;
    private String username;
    private String nickname;
    private List<String> roles;
}
