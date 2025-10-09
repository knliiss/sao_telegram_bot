package dev.knalis.sao_telegram_bot.command;

import dev.knalis.sao_telegram_bot.bot.BotHandler;
import dev.knalis.sao_telegram_bot.client.dto.UserDTO;
import dev.knalis.sao_telegram_bot.model.AllowRequest;
import dev.knalis.sao_telegram_bot.model.AllowResponse;
import dev.knalis.sao_telegram_bot.service.TelegramSenderService;
import org.springframework.stereotype.Component;

@Component
public abstract class BotCommand extends BotHandler {

    public BotCommand(TelegramSenderService senderService) {
        super(senderService);
    }

    public abstract void execute(CommandArgs commandArgs);

    Command getCommandAnnotation() {
        return this.getClass().getAnnotation(Command.class);
    }

    public String getUsage() {
        Command cmdAnnotation = getCommandAnnotation();
        if (cmdAnnotation != null) {
            StringBuilder usage = new StringBuilder("/" + cmdAnnotation.value());

            for (int i = 0; i < cmdAnnotation.minArgs(); i++) {
                usage.append(" <arg").append(i + 1).append(">");
            }

            if (cmdAnnotation.maxArgs() == Integer.MAX_VALUE) {
                usage.append(" [arg").append(cmdAnnotation.minArgs() + 1).append(" ...]");
            } else {
                for (int i = cmdAnnotation.minArgs(); i < cmdAnnotation.maxArgs(); i++) {
                    usage.append(" [arg").append(i + 1).append("]");
                }
            }

            return usage.toString();
        }
        return "";
    }

    public AllowResponse isUserAllowed(AllowRequest allowRequest) {
        AllowResponse allowResponse = new AllowResponse();
        UserDTO user = allowRequest.getUser();
        String[] allowedRoles = getAllowedRoles();
        if (user.getRoles().contains("BANNED")) {
            allowResponse.setAllowed(false);
            allowResponse.setReason("Вы забанены в Боте.\n Если это ошибка, обратитесь к создателю на кристаликсе.");
        } else if (user.getRoles().stream().noneMatch(role -> {
            for (String allowedRole : allowedRoles) {
                if (role.equalsIgnoreCase(allowedRole)) {
                    return true;
                }
            }
            return false;
        })) {
            allowResponse.setAllowed(false);
            allowResponse.setReason("У вас нет прав для выполнения этой команды.");
        } else if (allowRequest.getArgs().length < getMinArgs() || allowRequest.getArgs().length > getMaxArgs()) {
            allowResponse.setAllowed(false);
            allowResponse.setReason("Неверное количество аргументов.\nИспользование: " + getUsage());
        } else {
            allowResponse.setAllowed(true);
        }
        return allowResponse;
    }

    public String[] getAllowedRoles() {
        Command cmdAnnotation = getCommandAnnotation();
        if (cmdAnnotation != null) {
            return cmdAnnotation.allowedRoles();
        }
        return new String[]{"USER"};
    }

    public int getMinArgs() {
        Command cmdAnnotation = getCommandAnnotation();
        if (cmdAnnotation != null) {
            return cmdAnnotation.minArgs();
        }
        return 0;
    }

    public int getMaxArgs() {
        Command cmdAnnotation = getCommandAnnotation();
        if (cmdAnnotation != null) {
            return cmdAnnotation.maxArgs();
        }
        return Integer.MAX_VALUE;
    }

    public String[] getAliases() {
        Command cmdAnnotation = getCommandAnnotation();
        if (cmdAnnotation != null) {
            return cmdAnnotation.aliases();
        }
        return new String[]{};
    }

    public  String getDescription() {
        Command cmdAnnotation = getCommandAnnotation();
        if (cmdAnnotation != null) {
            return cmdAnnotation.description();
        }
        return "";
    }

    public String getValue() {
        Command cmdAnnotation = getCommandAnnotation();
        if (cmdAnnotation != null) {
            return cmdAnnotation.value();
        }
        return "";
    }

}
