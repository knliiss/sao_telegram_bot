package dev.knalis.sao_telegram_bot.client.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GangDTO {
    private Long id;
    private String name;
    private Boolean open;
    private Integer memberLimit;
    private GangMemberDTO owner;
    private List<GangMemberDTO> members;
}
