package dev.knalis.sao_telegram_bot.command;

import dev.knalis.sao_telegram_bot.bot.BotHandler;
import dev.knalis.sao_telegram_bot.client.dto.UserDTO;
import org.springframework.stereotype.Component;

@Component
public interface BotCommand extends BotHandler {

    String getCommand();
    String getDescription();
    String[] getAliases();
    String[] getAllowedRoles();
    void execute(UserDTO executor, String[] args);

}
