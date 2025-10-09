package dev.knalis.sao_telegram_bot.callback.annotation;

import dev.knalis.sao_telegram_bot.bot.BotHandler;
import dev.knalis.sao_telegram_bot.service.TelegramSenderService;

public abstract class AbstractCallBackController extends BotHandler {
    public AbstractCallBackController(TelegramSenderService senderService) {
        super(senderService);
    }
}
