package dev.knalis.sao_telegram_bot.command.impl.general;

import dev.knalis.sao_telegram_bot.command.BotCommand;
import dev.knalis.sao_telegram_bot.command.Command;
import dev.knalis.sao_telegram_bot.command.CommandArgs;
import dev.knalis.sao_telegram_bot.service.ConsumerService;
import dev.knalis.sao_telegram_bot.service.TelegramSenderService;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Command(value = "/cancel", description = "Отменить ожидание аргумента", maxArgs = 0)
public class CancelCommand extends BotCommand {

    ConsumerService consumerService;

    public CancelCommand(TelegramSenderService senderService, ConsumerService consumerService) {
        super(senderService);
        this.consumerService = consumerService;
    }

    @Override
    public void execute(CommandArgs commandArgs) {
        long chatId = commandArgs.getExecutor().getId();
        if (consumerService.hasConsumer(chatId)) {
            consumerService.removeConsumer(chatId);
            sendMessage(chatId, "❌ Ожидание отменено.");
        } else {
            sendMessage(chatId, "ℹ️ Нет активного ожидания.");
        }
    }
}
