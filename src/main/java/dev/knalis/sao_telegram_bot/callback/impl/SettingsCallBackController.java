package dev.knalis.sao_telegram_bot.callback.impl;

import dev.knalis.sao_telegram_bot.callback.CallBackInfo;
import dev.knalis.sao_telegram_bot.callback.annotation.AbstractCallBackController;
import dev.knalis.sao_telegram_bot.callback.annotation.CallBackController;
import dev.knalis.sao_telegram_bot.callback.annotation.CallBackMethod;
import dev.knalis.sao_telegram_bot.callback.annotation.PathVariable;
import dev.knalis.sao_telegram_bot.composer.ComposerContext;
import dev.knalis.sao_telegram_bot.service.MenuService;
import dev.knalis.sao_telegram_bot.service.SettingsService;
import dev.knalis.sao_telegram_bot.service.TelegramSenderService;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;

@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@CallBackController("settings/{userId}")
public class SettingsCallBackController extends AbstractCallBackController {
    
    MenuService menuService;
    private final SettingsService settingsService;
    
    public SettingsCallBackController(TelegramSenderService senderService, MenuService menuService, SettingsService settingsService) {
        super(senderService);
        this.menuService = menuService;
        this.settingsService = settingsService;
    }
    
    @CallBackMethod("/{category}")
    public void getMenu(@PathVariable("userId") long userId, @PathVariable("category") String category, CallBackInfo info) {
        var context = new ComposerContext(userId);
        context.put("category", category);
        var messageId = info.getMessageId();
        var sendMessage = menuService.getSettingsMenu(context);
        editMessage(userId, messageId, sendMessage);
    }
    
    @CallBackMethod("/update/{category}/all/{state}")
    public void updateAllSettings(@PathVariable("userId") long userId, @PathVariable("category") String category, @PathVariable("state") boolean state, CallBackInfo info) {
        settingsService.updateAllByCategorySettings(userId, category, state);
        getMenu(userId, category, info);
    }
    
    @CallBackMethod("/update/{category}/one/{type}")
    public void toggleSetting(@PathVariable("userId") long userId, @PathVariable("category") String category, @PathVariable("type") String type, CallBackInfo info) {
        settingsService.toggleSetting(userId, type);
        getMenu(userId, category, info);
    }
}
