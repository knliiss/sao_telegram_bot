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
@CallBackController("settings/{userId}")
public class SettingsCallBackController extends AbstractCallBackController {
    
    MenuService menuService;
    
    public SettingsCallBackController(TelegramSenderService senderService, MenuService menuService) {
        super(senderService);
        this.menuService = menuService;
    }
    
    @CallBackMethod()
    private void getMenu(@PathVariable("userId") long userId, CallBackInfo info) {
        var context = new ComposerContext(userId);
        var messageId = info.getMessageId();
        var sendMessage = menuService.getSettingsMenu(context);
        var editMessage = editMessage(userId, messageId, sendMessage);
    }
}
