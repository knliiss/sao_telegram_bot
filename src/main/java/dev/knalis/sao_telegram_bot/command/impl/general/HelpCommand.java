package dev.knalis.sao_telegram_bot.command.impl;

import dev.knalis.sao_telegram_bot.client.dto.UserDTO;
import dev.knalis.sao_telegram_bot.command.BotCommand;
import dev.knalis.sao_telegram_bot.command.Command;
import dev.knalis.sao_telegram_bot.command.CommandArgs;
import dev.knalis.sao_telegram_bot.service.CommandService;
import dev.knalis.sao_telegram_bot.service.TelegramSenderService;
import org.springframework.context.annotation.Lazy;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Command(value = "/help", description = "Получить справочник по командам", aliases = {"/h", "/?"}, minArgs = 0, maxArgs = 0)
public class HelpCommand extends BotCommand {

    CommandService commandService;

    public HelpCommand(TelegramSenderService senderService, @Lazy CommandService commandService) {
        super(senderService);
        this.commandService = commandService;
    }

    @Override
    public void execute(CommandArgs args) {
        var executor = args.getExecutor();
        List<BotCommand> commands = commandService.getCommands();
        String message = format(commands);
        sendMessage(executor.getId(), message);
    }

    private String format(List<BotCommand> commands) {
        StringBuilder builder = new StringBuilder();
        builder.append("*📖 Справочник по командам*\n\n");
        builder.append("Каждая команда может иметь сокращения (алиасы) и ограничения по аргументам.\n\n");

        for (BotCommand command : commands) {
            builder.append("*")
                    .append(command.getValue())
                    .append("* ");

            if (command.getAliases() != null && command.getAliases().length > 0) {
                builder.append("_")
                        .append(formatAliases(command))
                        .append("_");
            }

            builder.append("\n")
                    .append("— ")
                    .append(command.getDescription() != null ? command.getDescription() : "Без описания")
                    .append("\n");

            if (command.getUsage() != null && !command.getUsage().isEmpty()) {
                builder.append("Пример: `")
                        .append(command.getUsage())
                        .append("`\n");
            }

            builder.append("\n");
        }

        builder.append("_Используйте команды без скобок и символов <>_\n");
        return builder.toString();
    }

    private String formatAliases(BotCommand command) {
        return Arrays.stream(command.getAliases())
                .map(a -> "`" + a + "`")
                .collect(Collectors.joining(", "));
    }
}

