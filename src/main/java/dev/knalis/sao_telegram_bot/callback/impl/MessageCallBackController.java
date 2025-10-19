package dev.knalis.sao_telegram_bot.callback.impl;

import dev.knalis.sao_telegram_bot.callback.CallBackInfo;
import dev.knalis.sao_telegram_bot.callback.annotation.AbstractCallBackController;
import dev.knalis.sao_telegram_bot.callback.annotation.CallBackController;
import dev.knalis.sao_telegram_bot.callback.annotation.CallBackMethod;
import dev.knalis.sao_telegram_bot.callback.annotation.PathVariable;
import dev.knalis.sao_telegram_bot.composer.ComposerContext;
import dev.knalis.sao_telegram_bot.service.MenuService;
import dev.knalis.sao_telegram_bot.service.TelegramSenderService;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;

@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@CallBackController("message")
public class MessageCallBackController extends AbstractCallBackController {

    MenuService menuService;

    public MessageCallBackController(TelegramSenderService senderService, MenuService menuService) {
        super(senderService);
        this.menuService = menuService;
    }

    @CallBackMethod("/delete")
    public void delete(CallBackInfo info) {
        deleteMessage(info.getUser().getId(), info.getMessageId());
    }

    @CallBackMethod("/menu")
    public void getMenu(CallBackInfo info) {
        int messageId = info.getMessageId();
        long chatId = info.getUser().getId();
        var context = new ComposerContext(chatId);
        var message = menuService.getMenu(context);
        editMessage(chatId, messageId, message);
    }

    @CallBackMethod("/test/{value}")
    public void test(@PathVariable("value") String value, CallBackInfo info) {
        sendMessage(info.getUser().getId(), value);
    }
}
