package dev.knalis.sao_telegram_bot.service;

import dev.knalis.sao_telegram_bot.client.dto.UserCreateRequest;
import dev.knalis.sao_telegram_bot.client.dto.UserDTO;
import dev.knalis.sao_telegram_bot.command.BotCommand;
import dev.knalis.sao_telegram_bot.command.Command;
import dev.knalis.sao_telegram_bot.command.CommandArgs;
import dev.knalis.sao_telegram_bot.model.AllowRequest;
import dev.knalis.sao_telegram_bot.model.AllowResponse;
import jakarta.annotation.PostConstruct;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.HashMap;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CommandService {

    @Getter
    List<BotCommand> commands;
    UserService userService;
    TelegramSenderService senderService;
    HashMap<String, BotCommand> botCommands = new HashMap<>();
    private final ConsumerService consumerService;

    @PostConstruct
    public void init() {
        commands.stream()
                .filter(cmd -> cmd.getClass().getAnnotation(Command.class) != null)
                .forEach(cmd -> {
                    Command cmdAnnotation = cmd.getClass().getAnnotation(Command.class);
                    botCommands.put(cmdAnnotation.value().toLowerCase(), cmd);
                    for (String alias : cmdAnnotation.aliases()) {
                        botCommands.put(alias.toLowerCase(), cmd);
                        log.info("Added alias: {}", alias);
                    }
                });
    }

    public void execute(Update update) {
        String messageText = update.getMessage().getText();
        String commandText = messageText.split(" ")[0];
        String[] args = messageText.contains(" ") ? messageText.substring(messageText.indexOf(" ") + 1).split(" ") : new String[]{};
        Long chatId = update.getMessage().getChatId();

        UserDTO user = getOrCreateUser(chatId, update.getMessage().getFrom().getUserName());
        BotCommand command = botCommands.get(commandText.toLowerCase());

        if (!validateCommand(command, commandText, args, user, chatId)) {
            return;
        }

        var messageId = update.getMessage().getMessageId();
        var commandArg = CommandArgs.builder().executor(user).args(args).messageId(messageId).build();
        command.execute(commandArg);
    }

    private UserDTO getOrCreateUser(Long chatId, String username) {
        UserDTO user = userService.getUser(chatId);
        if (user == null) {
            UserCreateRequest request = UserCreateRequest.builder()
                    .id(chatId)
                    .username(username)
                    .build();
            user = userService.createUser(request);
            if (user == null) {
                senderService.sendMessage(chatId, "Ошибка при создании пользователя. Попробуйте еще раз.");
                throw new RuntimeException(String.format("Ошибка при создании пользователя {}({}).", username, chatId));
            }
        }
        return user;
    }

    private boolean validateCommand(BotCommand command, String commandText, String[] args, UserDTO user, Long chatId) {
        if (!commandText.startsWith("/") ) {
            tryConsumer(chatId, commandText);
            return false;
        }

        if (command == null) {
            log.error("command not found for command {} \n List of valid commands: {}", commandText, botCommands.keySet());
            senderService.sendMessage(chatId, "Неизвестная команда. Используйте /help для списка команд.");
            return false;
        }

        AllowResponse allowResponse = command.isUserAllowed(new AllowRequest(user, commandText, args));
        if (!allowResponse.isAllowed()) {
            command.sendErrorMessage(chatId, allowResponse.getReason());
            return false;
        }
        return true;
    }

    private void tryConsumer(long chatId, String argument) {
        if (consumerService.hasConsumer(chatId)) {
            consumerService.executeConsumer(chatId, argument);
        }
    }

}
