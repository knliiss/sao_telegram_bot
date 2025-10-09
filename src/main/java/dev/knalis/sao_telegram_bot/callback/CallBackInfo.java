package dev.knalis.sao_telegram_bot.callback;

import dev.knalis.sao_telegram_bot.client.dto.UserDTO;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class CallBackInfo {
    UserDTO user;
    int messageId;
    long timestamp;
}
