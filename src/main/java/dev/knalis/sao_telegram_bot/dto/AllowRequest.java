package dev.knalis.sao_telegram_bot.model;

import dev.knalis.sao_telegram_bot.client.dto.UserDTO;
import lombok.Data;

@Data
public class AllowRequest {
    private UserDTO user;
    private String command;
    private String[] args;

    public AllowRequest(UserDTO user, String commandText, String[] args) {
        this.user = user;
        this.command = commandText;
        this.args = args;
    }
}
