package dev.knalis.sao_telegram_bot.command.impl.general;

import dev.knalis.sao_telegram_bot.command.BotCommand;
import dev.knalis.sao_telegram_bot.command.Command;
import dev.knalis.sao_telegram_bot.command.CommandArgs;
import dev.knalis.sao_telegram_bot.service.CommandService;
import dev.knalis.sao_telegram_bot.service.TelegramSenderService;
import org.springframework.context.annotation.Lazy;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Command(
        value = "/help",
        description = "Получить справочник по командам",
        aliases = {"/h", "/?"},
        minArgs = 0,
        maxArgs = 0
)
public class HelpCommand extends BotCommand {

    private final CommandService commandService;

    public HelpCommand(TelegramSenderService senderService, @Lazy CommandService commandService) {
        super(senderService);
        this.commandService = commandService;
    }

    @Override
    public void execute(CommandArgs args) {
        var executor = args.getExecutor();
        var text = composeText(executor.getRoles());
        sendMessage(executor.getId(), text);
    }

    public String composeText(List<String> roles) {
        var commands = commandService.getCommands();
        StringBuilder builder = new StringBuilder();

        builder.append("<b>📖 Справочник по командам</b>\n\n")
                .append("Каждая команда может иметь сокращения (алиасы) и ограничения по аргументам.\n\n");

        for (var command : commands) {
            if (command.getAllowedRoles() != null && command.getAllowedRoles().length > 0) {
                boolean hasAccess = Arrays.stream(command.getAllowedRoles())
                        .anyMatch(roles::contains);
                if (!hasAccess) continue;
            }

            builder.append("<b>")
                    .append(command.getValue())
                    .append("</b> ");

            if (command.getAliases() != null && command.getAliases().length > 0) {
                builder.append("<i>")
                        .append(formatAliases(command))
                        .append("</i>");
            }

            builder.append("\n— ")
                    .append(command.getDescription() != null ? command.getDescription() : "Без описания")
                    .append("\n");

            if (command.getUsage() != null && !command.getUsage().isEmpty()) {
                builder.append("Пример: <b>")
                        .append(command.getUsage())
                        .append("</b>\n");
            }

            builder.append("\n");
        }

        builder.append("<i>Используйте команды без скобок и символов </i>");
        return builder.toString();
    }

    private String formatAliases(BotCommand command) {
        return Arrays.stream(command.getAliases())
                .map(a -> "<code>" + a + "</code>")
                .collect(Collectors.joining(", "));
    }
}
