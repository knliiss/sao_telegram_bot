package dev.knalis.sao_telegram_bot.callback.impl;

import dev.knalis.sao_telegram_bot.callback.CallBackInfo;
import dev.knalis.sao_telegram_bot.callback.annotation.AbstractCallBackController;
import dev.knalis.sao_telegram_bot.callback.annotation.CallBackController;
import dev.knalis.sao_telegram_bot.callback.annotation.CallBackMethod;
import dev.knalis.sao_telegram_bot.callback.annotation.PathVariable;
import dev.knalis.sao_telegram_bot.service.TelegramSenderService;

@CallBackController("message")
public class MessageCallBackController extends AbstractCallBackController {

    public MessageCallBackController(TelegramSenderService senderService) {
        super(senderService);
    }

    @CallBackMethod("/delete")
    public void delete(CallBackInfo info) {
        deleteMessage(info.getUser().getId(), info.getMessageId());
    }

    @CallBackMethod("/test/{value}")
    public void test(@PathVariable("value") String value, CallBackInfo info) {
        sendMessage(info.getUser().getId(), value);
    }
}
